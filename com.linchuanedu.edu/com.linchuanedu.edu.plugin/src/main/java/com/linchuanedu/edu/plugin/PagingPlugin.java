package com.linchuanedu.edu.plugin;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ZHOUDF2 on 2017-9-28.
 */
@Intercepts({
        @Signature(type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class})})
public class PagingPlugin implements Interceptor {

    private Integer defaultPage;
    private Integer defaultPageSize;
    private Boolean defaultUseFlag;
    private Boolean defaultCheckFlag;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler stmtHandler = getUnProxyObject(invocation);
        MetaObject metaStatementHandler = SystemMetaObject.forObject(stmtHandler);
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        //不是select语句.
        if (!this.checkSelect(sql)) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        PageParams pageParams = null;
        if (parameterObject instanceof Map) {
            Map parameterMap = (Map) parameterObject;
            pageParams = (PageParams) parameterMap.get("$pageParams");
        } else if (parameterObject instanceof PageParams) {
            pageParams = (PageParams) parameterObject;
        }
        if (pageParams == null) {
            throw new Exception("没有获得分页参数！！，请正确使用分页参数！！");
        }
        //获取参数.
        Integer pageNum = pageParams.getPage() == null? this.defaultPage : pageParams.getPage();
        Integer pageSize = pageParams.getPageSize() == null? this.defaultPageSize : pageParams.getPageSize();
        Boolean useFlag = pageParams.getUseFlag() == null? this.defaultUseFlag : pageParams.getUseFlag();
        Boolean checkFlag = pageParams.getCheckFlag() == null? this.defaultCheckFlag : pageParams.getCheckFlag();
        if (!useFlag) {  //不使用分页插件.
            return invocation.proceed();
        }
        int total = this.getTotal(invocation, metaStatementHandler, boundSql);
        pageParams.setTotal(total);
        //计算总页数.
        int totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        pageParams.setTotalPage(totalPage);
        //检查当前页码的有效性.
        this.checkPage(checkFlag, pageNum, totalPage);
        //修改sql
        this.changeSQL(metaStatementHandler, boundSql, pageNum, pageSize);
        //执行查询，得到最后的结果.
        Object data = invocation.proceed();
//        PageDataPO pageData = this.dealPageData(data, pageTotal, total, pageSize, pageNum);
        return data;
    }


    /**
     * 判断是否sql语句.
     * @param sql
     * @return
     */
    private boolean checkSelect(String sql) {
        String trimSql = sql.trim();
        int idx = trimSql.toLowerCase().indexOf("select");
        return idx == 0;
    }

    /**
     * 检查当前页码的有效性.
     * @param checkFlag
     * @param pageNum
     * @param pageTotal
     * @throws Throwable
     */
    private void checkPage(Boolean checkFlag, Integer pageNum, Integer pageTotal) throws Throwable  {
        if (checkFlag) {
            //检查页码page是否合法.
            if (pageNum > pageTotal) {
                throw new Exception("查询失败，查询页码【" + pageNum + "】大于总页数【" + pageTotal + "】！！");
            }
        }
    }


    /**
     * 修改当前查询的SQL
     * @param metaStatementHandler
     * @param boundSql
     * @param page
     * @param pageSize
     */
    private void changeSQL(MetaObject metaStatementHandler, BoundSql boundSql, int page, int pageSize) {
        //获取当前需要执行的SQL
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        /**
         * TODO 这里使用的是MySQL其他数据库需要修改.
         * 根据你的数据库，修改分页的SQL
         */
        String newSql = "select * from (" + sql + ") $_paging_table limit " + (page - 1) * pageSize + ", " + pageSize;
        //修改当前需要执行的SQL
        metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
    }

    /**
     * 获取综述.
     *
     * @param ivt Invocation
     * @param metaStatementHandler statementHandler
     * @param boundSql sql
     * @return sql查询总数.
     * @throws Throwable 异常.
     */
    private int getTotal(Invocation ivt, MetaObject metaStatementHandler, BoundSql boundSql) throws Throwable {
        //获取当前的mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        //配置对象
        Configuration cfg = mappedStatement.getConfiguration();
        //当前需要执行的SQL
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        /**
         * TODO 如果是其他的数据库需要按你数据库的SQL规范改写.
         * 改写为统计总数的SQL
         */
        String countSql = "select count(*) as total from (" + sql + ") $_paging";
        //获取拦截方法参数，我们知道是Connection对象.
        Connection connection = (Connection) ivt.getArgs()[0];
        PreparedStatement ps = null;
        int total = 0;
        try {
            //预编译统计总数SQL
            ps = connection.prepareStatement(countSql);
            //构建统计总数SQL
            BoundSql countBoundSql = new BoundSql(cfg, countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            //构建MyBatis的ParameterHandler用来设置总数Sql的参数。
            ParameterHandler handler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBoundSql);
            //设置总数SQL参数
            handler.setParameters(ps);
            //执行查询.
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total");
            }
        } finally {
            //这里不能关闭Connection否则后续的SQL就没法继续了。
            if (ps != null && ps.isClosed()) {
                ps.close();
            }
        }
        System.err.println("总条数：" + total);
        return total;
    }

    /**
     * 从代理对象中分离出真实对象.
     *
     * @param ivt --Invocation
     * @return 非代理StatementHandler对象
     */
    private StatementHandler getUnProxyObject(Invocation ivt) {
        StatementHandler statementHandler = (StatementHandler) ivt.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过循环可以分离出最原始的的目标类)
        Object object = null;
        while (metaStatementHandler.hasGetter("h")) {
            object = metaStatementHandler.getValue("h");
        }
        if (object == null) {
            return statementHandler;
        }
        return (StatementHandler) object;
    }

    @Override
    public Object plugin(Object statementHandler) {
        return Plugin.wrap(statementHandler, this);
    }

    @Override
    public void setProperties(Properties props) {
        String strDefaultPage = props.getProperty("default.page", "1");
        String strDefaultPageSize = props.getProperty("default.pageSize", "50");
        String strDefaultUseFlag = props.getProperty("default.useFlag", "false");
        String strDefaultCheckFlag = props.getProperty("default.checkFlag", "false");
        this.defaultPage = Integer.parseInt(strDefaultPage);
        this.defaultPageSize = Integer.parseInt(strDefaultPageSize);
        this.defaultUseFlag = Boolean.parseBoolean(strDefaultUseFlag);
        this.defaultCheckFlag = Boolean.parseBoolean(strDefaultCheckFlag);
    }


}

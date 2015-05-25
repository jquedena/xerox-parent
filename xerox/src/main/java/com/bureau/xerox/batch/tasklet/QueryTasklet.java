package com.bureau.xerox.batch.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;

import com.everis.core.exception.BussinesException;
import com.everis.util.DBUtilSpring;

public class QueryTasklet extends AbstractTasklet {

    private String dataSourceName;
    private String query;

    @Override
    protected void doExecute(StepContribution contribution, ChunkContext chunkContext) throws TaskletException {
        try {
            if (DBUtilSpring.getInstance().execute(dataSourceName, query)) {
                setExitStatus(ExitStatus.COMPLETED);
                contribution.incrementWriteCount(1);
            }
        } catch(BussinesException e) {
            throw new TaskletException(e);
        }
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("QueryTasklet [dataSourceName=");
        builder.append(dataSourceName);
        builder.append(", query=");
        builder.append(query);
        builder.append("]");
        return builder.toString();
    }

}

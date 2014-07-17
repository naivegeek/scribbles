package com.vijayrc.tasker.api;

import com.vijayrc.tasker.config.TestMaker;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ChunkParser;
import org.glassfish.jersey.client.ChunkedInput;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.junit.Test;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;

import static com.vijayrc.tasker.config.TestMaker.target;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.join;

public class LogApiTest {
    private static Logger log = LogManager.getLogger(LogApiTest.class);

    @Test
    public void shouldReadFromChunkedResponseButWaitUntilEnd() throws Exception {
        AsyncInvoker invoker = target().path("/logs/2").request().async();
        Future<Response> future = invoker.get();
        while(!future.isDone()){
            String line= future.get().readEntity(String.class);
            log.info(line);
        }
    }
    @Test
    public void shouldReadFromChunkedResponsePieceMeal() throws Exception {
        Response response = target().path("/logs/2").request().get();
        ChunkedInput<String> chunkedInput = response.readEntity(new GenericType<ChunkedInput<String>>(){});
        chunkedInput.setParser(ChunkedInput.createParser("\n"));

        String chunk;
        while((chunk = chunkedInput.read()) != null)
            log.info(chunk+"|"+chunk.length());
    }

    @Test
    public void shouldReadFromServerEvents() throws Exception {
        EventInput eventInput = target().path("/logs/sse/1").request().get(EventInput.class);
        while(!eventInput.isClosed()){
            InboundEvent event = eventInput.read();
            if(event == null) break;
            log.info(event.getName()+"|"+event.readData(String.class));
        }
    }
}

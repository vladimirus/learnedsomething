package com.learnedsomething.biz.manager.task;

import static org.mockito.Mockito.verify;

import com.learnedsomething.biz.manager.LinkManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParallelTaskTest {
    private ParallelTask linkIndexer;

    @Mock
    private LinkManager linkManager;

    @Test
    public void startIndex() {
        // given
        linkIndexer = new ParallelTask(linkManager, "index");

        // when
        linkIndexer.run();

        // then
        verify(linkManager).index();
    }
}

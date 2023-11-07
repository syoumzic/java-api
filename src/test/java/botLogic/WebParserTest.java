package botLogic;

import botLogic.parser.WebParser;
import org.junit.*;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertThrows;

public class WebParserTest {
    /**
     * проверяет преобразование номера группы в id группы
     * @throws IOException
     */

    @Test
    public void testGroupIdParser() throws IOException {
        WebParser parser = new WebParser();

        var groups = new String[]{"МЕН-220201", "МЕН-220203", "РИ-311110", "РИ-311151", "РИС", "МЕН", "МЕН-2202012323"};
        var output = new String[]{"56393", "56415", "57108", "57022", null, null, null};

        Assert.assertEquals("56393", parser.getGroupId("МЕН-220201"));
        Assert.assertEquals("56415", parser.getGroupId("МЕН-220203"));
        Assert.assertEquals("57108", parser.getGroupId("РИ-311110"));
        Assert.assertEquals("57022", parser.getGroupId("РИ-311151"));

        assertThrows(NoSuchElementException.class, () -> parser.getGroupId("РИС"));
        assertThrows(NoSuchElementException.class, () -> parser.getGroupId("МЕН-3111234423510"));
    }
}

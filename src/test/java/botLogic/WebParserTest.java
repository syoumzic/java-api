package botLogic;

import org.json.simple.parser.ParseException;
import org.junit.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WebParserTest {
    @Test
    public void testGroupIdParser() throws IOException {
        WebParser parser = new WebParser();

        var groups = new String[]{"МЕН-220201", "МЕН-220203", "РИ-311110", "РИ-311151", "РИС", "МЕН", "МЕН-2202012323"};
        var output = new String[]{"56393", "56415", "57108", "57022", null, null, null};

        for(int i = 0; i < groups.length; i++)
            Assert.assertEquals(output[i], parser.getGroupId(groups[i]));
    }
}

package botLogic;

import org.junit.*;

public class MainTest{
    @Test
    public void logicTest(){
        Logic logic = new Logic();

        String []input = {"хей", "привет", "ты чего повторяешь?", "прекрати!", "ну хватит", "лалала", "лыдал", "q2"};
        String []output = {"хей", "привет", "ты чего повторяешь?", "прекрати!", "ну хватит", "лалала", "лыдал", "q2"};

        for(int i = 0; i < input.length; i++)
            Assert.assertEquals(output[i], logic.processMessage("none", input[i]));
    }
}

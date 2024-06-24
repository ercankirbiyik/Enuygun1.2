package test;

import org.testng.annotations.Test;
import step.BaseSteps;

import java.io.IOException;

public class EnuygunTests extends BaseSteps {

    public EnuygunTests() throws IOException {
    }

    @Test
    public void Test(){
        goToEnuygun();
        deneme();
    }

}

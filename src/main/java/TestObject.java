public class TestObject {
    final String testProp;

    public String getTestProp() {
        return testProp;
    }

    public TestObject(String testProp) {
        this.testProp = testProp;
    }

    public TestObject() {
        this("default");
    }
}
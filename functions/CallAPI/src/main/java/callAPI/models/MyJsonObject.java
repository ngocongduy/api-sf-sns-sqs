package CallAPI.src.main.java.callAPI.models;

public class MyJsonObject {
        private String Id;
        private String Value;

//        public MyJsonObject(String id, String value) {
//            Id = id;
//            Value = value;
//        }

        public String getId() {
            return Id;
        }

        public String getValue() {
            return Value;
        }

        public void setId(String id) {
            Id = id;
        }

        public void setValue(String value) {
            Value = value;
        }

    @Override
    public String toString() {
        return "MyJsonObject{" +
                "Id='" + Id + '\'' +
                ", Value='" + Value + '\'' +
                '}';
    }
}

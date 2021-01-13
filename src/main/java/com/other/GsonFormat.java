package com.other;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * GsonFormat使用案例
 */
@Data
public class GsonFormat implements Serializable {

    /**
     * animals : {"dog":[{"name":"Rufus","breed":"labrador","count":1,"twoFeet":false},{"name":"Marty","breed":"whippet","count":1,"twoFeet":false}],"cat":{"name":"Matilda"}}
     */

    private AnimalsBean animals;

    @Data
    public static class AnimalsBean implements Serializable {
        /**
         * dog : [{"name":"Rufus","breed":"labrador","count":1,"twoFeet":false},{"name":"Marty","breed":"whippet","count":1,"twoFeet":false}]
         * cat : {"name":"Matilda"}
         */

        private CatBean cat;
        private List<DogBean> dog;

        @Data
        public static class CatBean implements Serializable {
            /**
             * name : Matilda
             */

            private String name;
        }

        @Data
        public static class DogBean implements Serializable {
            /**
             * name : Rufus
             * breed : labrador
             * count : 1
             * twoFeet : false
             */

            private String name;
            private String breed;
            private int count;
            private boolean twoFeet;
        }
    }
}

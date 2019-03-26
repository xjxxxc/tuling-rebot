package com.xjxxxc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 @author Jason Xu
 @DESCRIPTION 图灵机器人返回参数实体
 @create 2019/3/18
*/
@NoArgsConstructor
@Data
public class TulingReqBody {

    /**
     * reqType : 0
     * perception : {"inputText":{"text":"深圳的天气"},"inputImage":{"url":"imageUrl"},"selfInfo":{"location":{"city":"深圳","province":"广东","street":""}}}
     * userInfo : {"apiKey":"57231441c68642fe86998a360ec13643","userId":"123"}
     */

    private int reqType;
    private PerceptionBean perception;
    private UserInfoBean userInfo;

    @NoArgsConstructor
    @Data
    public static class PerceptionBean {
        /**
         * inputText : {"text":"深圳的天气"}
         * inputImage : {"url":"imageUrl"}
         * selfInfo : {"location":{"city":"深圳","province":"广东","street":""}}
         */

        private InputTextBean inputText;
        private InputImageBean inputImage;
        private SelfInfoBean selfInfo;

        @NoArgsConstructor
        @Data
        public static class InputTextBean {
            /**
             * text : 深圳的天气
             */

            private String text;
        }

        @NoArgsConstructor
        @Data
        public static class InputImageBean {
            /**
             * url : imageUrl
             */

            private String url;
        }

        @NoArgsConstructor
        @Data
        public static class SelfInfoBean {
            /**
             * location : {"city":"深圳","province":"广东","street":""}
             */

            private LocationBean location;

            @NoArgsConstructor
            @Data
            public static class LocationBean {
                /**
                 * city : 深圳
                 * province : 广东
                 * street :
                 */

                private String city;
                private String province;
                private String street;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class UserInfoBean {
        /**
         * apiKey : 57231441c68642fe86998a360ec13643
         * userId : 123
         */

        private String apiKey;
        private String userId;
    }
}

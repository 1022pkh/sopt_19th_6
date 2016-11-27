package com.pkh.sopt_19th_6.detail;

/**
 * Created by kh on 2016. 11. 26..
 */
public class DetailResult {
    public DetailData result;

    class DetailData{
        public int id;
        public String title;
        public String contents;
        public String image_url;

        public DetailData(int id, String title, String contents, String image_url) {
            this.id = id;
            this.title = title;
            this.contents = contents;
            this.image_url = image_url;
        }
    }
}

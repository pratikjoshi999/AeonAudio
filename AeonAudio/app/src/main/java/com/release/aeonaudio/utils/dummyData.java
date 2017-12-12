package com.release.aeonaudio.utils;

import com.release.aeonaudio.model.SingleItemModel;

import java.util.ArrayList;

/**
 * Created by Muvi on 6/14/2017.
 */

public class dummyData {
    public static ArrayList<SingleItemModel>  dummyDataList() {
        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
//        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
//        final ActionBar ab = activity.getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.menu_ic);
//        ab.setDisplayHomeAsUpEnabled(true);

        final String urlHeyma = "http://musicgoo.net/download-music.php?song_id=3478";
        String urlSuite = "https://dl.pagal.link/upload_file/5570/6757/Latest%20Bollywood%20Hindi%20Mp3%20Songs%20-%202017/Tubelight%20%282017%29%20Hindi%20Movie%20Mp3%20Songs/01%20Radio%20-%20Tubelight%20%28Kamaal%20Khan%29%20320Kbps.mp3";
        String urlgetlow = "https://dl.pagal.link/upload_file/5570/Eng%20Pop/Collection%202015/Get%20Low%20%28Furious%207%29%20Dillon%20Francis%20n%20DJ%20Snake%20320Kbps.mp3";
        String urlPhirv = "http://sd.yoyodesi.com/128/482473/I%20m%20The%20One%20Ft%20Justin%20Bieber%20Quavo%20Chance%20The%20Rapper%20%20Lil%20Wayne%20-%20DJ%20Khaled%20(DJJOhAL.Com).mp3";
        String urlSou = "https://dl.pagal.link/upload_file/5570/6757/Latest%20Bollywood%20Hindi%20Mp3%20Songs%20-%202017/Behen%20Hogi%20Teri%20%282017%29%20Hindi%20Movie%20Mp3%20Songs/01%20Tera%20Hoke%20Rahoon%20%28Arijit%20Singh%29%20320Kbps.mp3";
        String Raabta1 = "http://mp3dl.djmaza.click/download/eea7670c027bad4250c924b4df483530";
        String url_Raabta = "https://dl.pagal.link/upload_file/5570/6757/Latest%20Bollywood%20Hindi%20Mp3%20Songs%20-%202017/Raabta%20%282017%29%20Hindi%20Movie%20Mp3%20Songs/05%20Main%20Tera%20Boyfriend%20-%20Raabta%20%28Arijit%20Singh%29%20320Kbps.mp3";
        String url_purpose = "http://dl2.shirazsong.org/dl/music/94-10/best-of-november-2015/01%20-%20Justin%20Bieber%20-%20Purpose.mp3";
        //        Intent intent = new Intent(getActivity(), MusicPlayer.class);
//        intent.putExtra(EXTRA_MESSAGE, Uri.parse(url));
//        startActivity(intent);
        final String ff = "http://thefader-res.cloudinary.com/images/w_760,c_limit,f_auto,q_auto:best/fast8_qpwkhu/fate-of-the-furious-soundtrack-migos-young-thug.jpg";
        String Tubelight = "https://www.djmaza.life/storage/images/400/5201.jpg";
        String ffgetlow = "https://cccmurphysboro.files.wordpress.com/2012/05/get-low.jpg";
        String Raabta_img = "https://www.djmaza.life/storage/images/400/5205.jpg";
        String shapeofu = "https://www.djmaza.life/storage/images/400/5191.jpg";
        final String half1 = "http://lq.yoyodesi.com/covers/57967.jpg";
        final String Raabta = "https://www.djmaza.life/storage/images/400/5213.jpg";
        final String purpose = "http://ecx.images-amazon.com/images/I/51epu%2B3amVL._AC_AA160_.jpg";
        final String baby = "https://dl.pagal.link/upload_file/5570/5773/IndiPop%20Mp3%20Songs%20-%202017/Baby%20Marvake%20Maanegi%20-%20Raftaar%20-%20Mp3%20Song/thumb-Baby%20Marvake%20Maanegi%20-%20Raftaar%20-%20320Kbps.jpg";


        singleItem.add(new SingleItemModel("Fast & Furious", "GO LOW", ffgetlow, urlgetlow));
        singleItem.add(new SingleItemModel(" I m The One Ft Justin Bieber ", "Dj Khaled ", half1, urlPhirv));
        singleItem.add(new SingleItemModel("Lambiyaan Si Judaiyaan ", "Raabta", Raabta_img, Raabta1));
        singleItem.add(new SingleItemModel("Tera Hoke Rahoon", "Behen Hogi Teri", shapeofu, urlSou));
        singleItem.add(new SingleItemModel("Tubelight", "Radio", Tubelight, urlSuite));
        singleItem.add(new SingleItemModel("Main Tera Boyfriend", " Raabta  ", Raabta, url_Raabta));
        singleItem.add(new SingleItemModel("Fate and furious", "Hey maa", ff, urlHeyma));
//        data.add(new ImageLoader("Purpose", "Purpose", ff, urlHeyma));
        singleItem.add(new SingleItemModel("Purpose", "Purpose", purpose, url_purpose));
        return singleItem;
    }

}

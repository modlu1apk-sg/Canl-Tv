package com.example.data

import androidx.compose.ui.graphics.Color

data class TvChannel(
    val id: String,
    val name: String,
    val category: String,
    val streamUrl: String,
    val logoText: String,
    val colorHex: String
) {
    val composeColor: Color
        get() = try {
            Color(android.graphics.Color.parseColor(colorHex))
        } catch (e: Exception) {
            Color(0xFF1E88E5)
        }
}

object TvChannelProvider {
    val categories = listOf("Tümü", "Ulusal", "Haber", "Spor", "Çocuk & Belgesel", "Müzik & Kültür")

    val channels = listOf(
        // Ulusal (National)
        TvChannel("trt1", "TRT 1", "Ulusal", "https://trt.daioncdn.net/trt-1/master.m3u8", "TRT 1", "#E53935"),
        TvChannel("atv", "ATV", "Ulusal", "https://turkuvaz-live.daioncdn.net/atv/atv.m3u8", "ATV", "#FF5722"),
        TvChannel("kanald", "Kanal D", "Ulusal", "https://demiroren.daioncdn.net/kanald/kanald.m3u8", "KanalD", "#00BCD4"),
        TvChannel("showtv", "Show TV", "Ulusal", "https://ciner-live.daioncdn.net/showtv/showtv.m3u8", "ShowTV", "#E91E63"),
        TvChannel("startv", "Star TV", "Ulusal", "https://dyg.daioncdn.net/star/star.m3u8", "Star", "#9C27B0"),
        TvChannel("nowtv", "NOW TV", "Ulusal", "https://nowtv.daioncdn.net/nowtv/nowtv.m3u8", "NOW", "#3F51B5"),
        TvChannel("tv8", "TV 8", "Ulusal", "https://tv8.daioncdn.net/tv8/tv8.m3u8", "TV 8", "#FF9800"),
        TvChannel("tv8_5", "TV 8.5", "Ulusal", "https://tv8.daioncdn.net/tv8-5/tv8-5.m3u8", "TV8.5", "#FF3D00"),
        TvChannel("kanal7", "Kanal 7", "Ulusal", "https://kanal7.daioncdn.net/kanal7/kanal7.m3u8", "Kanal 7", "#4CAF50"),
        TvChannel("teve2", "Teve 2", "Ulusal", "https://demiroren.daioncdn.net/teve2/teve2.m3u8", "Teve2", "#009688"),
        TvChannel("tv360", "360 TV", "Ulusal", "https://turkuvaz-live.daioncdn.net/tv360/tv360.m3u8", "360 TV", "#795548"),
        TvChannel("ulketv", "Ülke TV", "Ulusal", "https://kanal7.daioncdn.net/ulketv/ulketv.m3u8", "ÜlkeTV", "#283593"),

        // Haber (News)
        TvChannel("trthaber", "TRT Haber", "Haber", "https://trt.daioncdn.net/trt-haber/master.m3u8", "TRT H.", "#D32F2F"),
        TvChannel("ntv", "NTV", "Haber", "https://dyg.daioncdn.net/ntv/ntv.m3u8", "NTV", "#0D47A1"),
        TvChannel("haberturk", "HaberTürk", "Haber", "https://ciner-live.daioncdn.net/haberturk/haberturk.m3u8", "HaberT.", "#1565C0"),
        TvChannel("cnnturk", "CNN Türk", "Haber", "https://dyg.daioncdn.net/cnnturk/cnnturk.m3u8", "CNN T.", "#C62828"),
        TvChannel("halktv", "Halk TV", "Haber", "https://halktv.daioncdn.net/halktv/halktv.m3u8", "HalkTV", "#0277BD"),
        TvChannel("szctv", "Sözcü TV", "Haber", "https://szctv.daioncdn.net/szctv/szctv.m3u8", "SZC TV", "#1B5E20"),
        TvChannel("tele1", "Tele 1", "Haber", "https://tele1.daioncdn.net/tele1/tele1.m3u8", "Tele1", "#37474F"),
        TvChannel("tv100", "TV 100", "Haber", "https://tv100.daioncdn.net/tv100/tv100.m3u8", "TV100", "#1E88E5"),
        TvChannel("ahaber", "A Haber", "Haber", "https://turkuvaz-live.daioncdn.net/ahaber/ahaber.m3u8", "A Haber", "#E65100"),
        TvChannel("tgrthaber", "TGRT Haber", "Haber", "https://tgrt.daioncdn.net/tgrthaber/tgrthaber.m3u8", "TGRT H.", "#00695C"),
        TvChannel("bloomberght", "Bloomberg HT", "Haber", "https://ciner-live.daioncdn.net/bloomberght/bloomberght.m3u8", "Bl.HT", "#2E7D32"),
        TvChannel("apara", "A Para", "Haber", "https://turkuvaz-live.daioncdn.net/apara/apara.m3u8", "A Para", "#3F51B5"),

        // Spor (Sports)
        TvChannel("trtspor", "TRT Spor", "Spor", "https://trt.daioncdn.net/trt-spor/master.m3u8", "TRT Sp.", "#2E7D32"),
        TvChannel("trtsporyildiz", "TRT Spor Yıldız", "Spor", "https://trt.daioncdn.net/trt-spor-yildiz/master.m3u8", "TRT Y.", "#00C853"),
        TvChannel("aspor", "A Spor", "Spor", "https://turkuvaz-live.daioncdn.net/aspor/aspor.m3u8", "A Spor", "#004D40"),
        TvChannel("fbtv", "Fenerbahçe TV", "Spor", "https://fenerbahce.daioncdn.net/fbtv/fbtv.m3u8", "FB TV", "#0D47A1"),

        // Çocuk & Belgesel (Kids & Documentary)
        TvChannel("trtcocuk", "TRT Çocuk", "Çocuk & Belgesel", "https://trt.daioncdn.net/trt-cocuk/master.m3u8", "TRT Ç.", "#FFB300"),
        TvChannel("trtbelgesel", "TRT Belgesel", "Çocuk & Belgesel", "https://trt.daioncdn.net/trt-belgesel/master.m3u8", "TRT B.", "#006064"),
        TvChannel("minikago", "Minika GO", "Çocuk & Belgesel", "https://turkuvaz-live.daioncdn.net/minikago/minikago.m3u8", "M. GO", "#8E24AA"),
        TvChannel("minikacocuk", "Minika Çocuk", "Çocuk & Belgesel", "https://turkuvaz-live.daioncdn.net/minikacocuk/minikacocuk.m3u8", "M. Çoc", "#D81B60"),
        TvChannel("dmax", "DMAX", "Çocuk & Belgesel", "https://dyg.daioncdn.net/dmax/dmax.m3u8", "DMAX", "#3E2723"),
        TvChannel("tlc", "TLC", "Çocuk & Belgesel", "https://dyg.daioncdn.net/tlc/tlc.m3u8", "TLC", "#00ACC1"),
        TvChannel("vavtv", "Vav TV", "Çocuk & Belgesel", "https://turkuvaz-live.daioncdn.net/vavtv/vavtv.m3u8", "Vav TV", "#757575"),
        TvChannel("diyanettv", "Diyanet TV", "Çocuk & Belgesel", "https://diyanettv.daioncdn.net/diyanettv/diyanettv.m3u8", "Diyanet", "#5D4037"),

        // Müzik & Kültür (Music & Culture)
        TvChannel("trtmuzik", "TRT Müzik", "Müzik & Kültür", "https://trt.daioncdn.net/trt-muzik/master.m3u8", "TRT M.", "#311B92"),
        TvChannel("trt2", "TRT 2", "Müzik & Kültür", "https://trt.daioncdn.net/trt-2/master.m3u8", "TRT 2", "#424242"),
        TvChannel("kralpoptv", "Kral Pop TV", "Müzik & Kültür", "https://dyg.daioncdn.net/kralpoptv/kralpoptv.m3u8", "KralP", "#4A148C"),
        TvChannel("powerturk", "Power Türk", "Müzik & Kültür", "https://power.daioncdn.net/powerturk/powerturk_720p.m3u8", "Power", "#880E4F"),
        TvChannel("trtworld", "TRT World", "Müzik & Kültür", "https://trt.daioncdn.net/trt-world/master.m3u8", "TRT W.", "#0F2027"),
        TvChannel("trtavaz", "TRT Avaz", "Müzik & Kültür", "https://trt.daioncdn.net/trt-avaz/master.m3u8", "TRT Av.", "#FF5722"),
        TvChannel("trtkurdi", "TRT Kurdi", "Müzik & Kültür", "https://trt.daioncdn.net/trt-kurdi/master.m3u8", "TRT K.", "#4CAF50"),
        TvChannel("trtarabi", "TRT Arabi", "Müzik & Kültür", "https://trt.daioncdn.net/trt-arabi/master.m3u8", "TRT Ar.", "#00BCD4")
    )
}

package com.aktechhub.livetvapp.repository

object ChannelRepository {
    fun getChannels(): List<String> {
        return listOf(
            "https://segment.yuppcdn.net/240122/news7/playlist.m3u8",
            "https://segment.yuppcdn.net/110322/polimernews/playlist.m3u8",
            "https://nw18live.cdn.jio.com/bpk-tv/News18_Tamil_Nadu_NW18_MOB/output01/News18_Tamil_Nadu_NW18_MOB-audio_98835_eng=98800-video=2293600.m3u8",
            "https://segment.yuppcdn.net/140622/isaiaruvi/playlist.m3u8",
            "https://segment.yuppcdn.net/050522/murasu/playlist.m3u8",
            "https://segment.yuppcdn.net/240122/siripoli/playlist.m3u8",
            "https://segment.yuppcdn.net/240122/kalaignartv/playlist.m3u8",
            "https://segment.yuppcdn.net/240122/news7/playlist.m3u8",
            "https://segment.yuppcdn.net/240122/puthiya/playlist.m3u8"
        )
    }
}
package com.aktechhub.livetvapp.repository

data class Channel(
    val number: Int,
    val name: String,
    val logoUrl: String, // Store the URL of the PNG/JPG image
    val streamUrl: String
)

object ChannelRepository {
    fun getChannels(): List<Channel> {
        return listOf(
            Channel(1, "News 7", "https://media.licdn.com/dms/image/v2/C4E1BAQHpI_TRDDPnMQ/company-background_10000/company-background_10000/0/1624365301168/news7tamil_cover?e=2147483647&v=beta&t=wP96J8vHeUFflursXk9E8S4-fUlACNoZqaVbRw_TYyE", "https://segment.yuppcdn.net/240122/news7/playlist.m3u8"),
            Channel(2, "Polimer News", "https://static.wikia.nocookie.net/logopedia/images/0/03/Polimer_News.jpeg", "https://segment.yuppcdn.net/110322/polimernews/playlist.m3u8"),
            Channel(3, "News 18 Tamil Nadu", "https://jiotvimages.cdn.jio.com/dare_images/images/News_18_Tamilnadu.png", "https://nw18live.cdn.jio.com/bpk-tv/News18_Tamil_Nadu_NW18_MOB/output01/News18_Tamil_Nadu_NW18_MOB-audio_98835_eng=98800-video=2293600.m3u8"),
            Channel(4, "Isai Aruvi", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnaFNZtbBDbGkIkRHMla_3hiejYRMyjqz0YA&s", "https://segment.yuppcdn.net/140622/isaiaruvi/playlist.m3u8"),
            Channel(5, "Murasu TV", "https://the-media-ant.mo.cloudinary.net/medias/58c281d4272d430ee15e37bd/1696935677478/_logo.svg?tx=w_350", "https://segment.yuppcdn.net/050522/murasu/playlist.m3u8"),
            Channel(6, "Siripoli", "https://i.ibb.co/cLkWgzY/New-Project.jpg", "https://segment.yuppcdn.net/240122/siripoli/playlist.m3u8"),
            Channel(7, "Kalaignar TV", "https://www.medianews4u.com/wp-content/uploads/2019/07/kalaignar.jpg", "https://segment.yuppcdn.net/240122/kalaignartv/playlist.m3u8"),
            Channel(8, "Puthiya Thalaimurai", "https://i.ytimg.com/vi/RUzgphgBRzI/hq720.jpg?v=65ba8a62&sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLBRqBZxkmSB1D0iLgvxwX2VJE17vw", "https://segment.yuppcdn.net/240122/puthiya/playlist.m3u8")
        )
    }
}

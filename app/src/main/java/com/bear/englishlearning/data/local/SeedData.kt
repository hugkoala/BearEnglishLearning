package com.bear.englishlearning.data.local

import com.bear.englishlearning.data.local.entity.Scenario
import com.bear.englishlearning.data.local.entity.Sentence

object SeedData {

    fun getScenarios(): List<Scenario> = listOf(
        Scenario(1, "At the Restaurant", "在餐廳", "daily", 1, "restaurant ordering food English conversation beginner"),
        Scenario(2, "Shopping for Clothes", "買衣服", "daily", 1, "shopping clothes store English conversation beginner"),
        Scenario(3, "Asking for Directions", "問路", "daily", 1, "asking directions street English conversation beginner"),
        Scenario(4, "At the Airport", "在機場", "travel", 1, "airport check in English conversation beginner"),
        Scenario(5, "Checking into a Hotel", "飯店入住", "travel", 1, "hotel check in English conversation beginner"),
        Scenario(6, "Seeing a Doctor", "看醫生", "daily", 1, "doctor visit clinic English conversation beginner"),
        Scenario(7, "At the Supermarket", "在超市", "daily", 1, "supermarket grocery shopping English conversation beginner"),
        Scenario(8, "Taking a Taxi", "搭計程車", "daily", 1, "taking taxi cab English conversation beginner"),
        Scenario(9, "At the Bank", "在銀行", "daily", 1, "bank account English conversation beginner"),
        Scenario(10, "Making a Phone Call", "打電話", "daily", 1, "phone call English conversation beginner"),
        Scenario(11, "Ordering Coffee", "點咖啡", "daily", 1, "ordering coffee cafe English conversation beginner"),
        Scenario(12, "At the Post Office", "在郵局", "daily", 1, "post office mail English conversation beginner"),
        Scenario(13, "At the Gym", "在健身房", "daily", 1, "gym fitness English conversation beginner"),
        Scenario(14, "Meeting New People", "認識新朋友", "daily", 1, "meeting people self introduction English conversation beginner"),
        Scenario(15, "At the Library", "在圖書館", "daily", 1, "library books English conversation beginner"),
        Scenario(16, "Weather Talk", "聊天氣", "daily", 1, "weather small talk English conversation beginner"),
        Scenario(17, "At the Movies", "看電影", "daily", 1, "movie theater cinema English conversation beginner"),
        Scenario(18, "Job Interview", "工作面試", "business", 1, "job interview English conversation beginner"),
        Scenario(19, "At the Pharmacy", "在藥局", "daily", 1, "pharmacy medicine English conversation beginner"),
        Scenario(20, "Taking Public Transport", "搭公車/地鐵", "daily", 1, "bus subway public transport English conversation beginner"),
        Scenario(21, "At a Party", "在派對", "daily", 1, "party socializing English conversation beginner"),
        Scenario(22, "Booking a Reservation", "預訂", "daily", 1, "booking reservation restaurant English conversation beginner"),
        Scenario(23, "Returning an Item", "退貨", "daily", 1, "returning item refund store English conversation beginner"),
        Scenario(24, "At the Hair Salon", "在美髮店", "daily", 1, "hair salon haircut English conversation beginner"),
        Scenario(25, "Talking About Hobbies", "聊興趣", "daily", 1, "hobbies interests English conversation beginner"),
        Scenario(26, "At the Gas Station", "在加油站", "daily", 1, "gas station English conversation beginner"),
        Scenario(27, "Emergency Situations", "緊急狀況", "daily", 1, "emergency help English conversation beginner"),
        Scenario(28, "At the Beach", "在海灘", "daily", 1, "beach vacation English conversation beginner"),
        Scenario(29, "Visiting a Friend", "拜訪朋友", "daily", 1, "visiting friend home English conversation beginner"),
        Scenario(30, "At the Office", "在辦公室", "business", 1, "office work English conversation beginner")
    )

    fun getSentences(): List<Sentence> = listOf(
        // 1. At the Restaurant
        Sentence(1, 1, "I'd like a table for two, please.", "我想要一張兩人桌，謝謝。", "注意 'table' 的 /eɪ/ 發音，'please' 尾音輕讀", 1),
        Sentence(2, 1, "Can I see the menu?", "我可以看菜單嗎？", "'menu' 重音在第一音節 /ˈmɛnjuː/", 2),
        Sentence(3, 1, "I'll have the chicken, please.", "我要雞肉，謝謝。", "'chicken' 注意 /tʃ/ 開頭的發音", 3),

        // 2. Shopping for Clothes
        Sentence(4, 2, "How much is this shirt?", "這件襯衫多少錢？", "'shirt' 注意 /ʃɜːrt/ 的捲舌音", 1),
        Sentence(5, 2, "Can I try this on?", "我可以試穿嗎？", "'try' 和 'on' 自然連讀", 2),
        Sentence(6, 2, "Do you have a smaller size?", "你有小一號的嗎？", "'smaller' 注意比較級 /ˈsmɔːlər/", 3),

        // 3. Asking for Directions
        Sentence(7, 3, "Excuse me, where is the train station?", "不好意思，火車站在哪裡？", "'excuse' 重音在第二音節 /ɪkˈskjuːz/", 1),
        Sentence(8, 3, "How do I get to the park?", "我怎麼去公園？", "'get to' 連讀為 /ɡetə/", 2),
        Sentence(9, 3, "Is it far from here?", "離這裡遠嗎？", "'far' 注意 /fɑːr/ 的長母音", 3),

        // 4. At the Airport
        Sentence(10, 4, "Where is the check-in counter?", "報到櫃檯在哪裡？", "'check-in' 重音在 'check'", 1),
        Sentence(11, 4, "I'd like a window seat, please.", "我想要靠窗的座位，謝謝。", "'window' 重音在第一音節", 2),
        Sentence(12, 4, "What time does the flight depart?", "飛機幾點起飛？", "'depart' 重音在第二音節 /dɪˈpɑːrt/", 3),

        // 5. Checking into a Hotel
        Sentence(13, 5, "I have a reservation under the name...", "我有預訂，名字是...", "'reservation' 重音在第三音節", 1),
        Sentence(14, 5, "What time is check-out?", "退房時間是幾點？", "'check-out' 重音在 'check'", 2),
        Sentence(15, 5, "Is breakfast included?", "有含早餐嗎？", "'included' 注意 /ɪnˈkluːdɪd/ 三音節", 3),

        // 6. Seeing a Doctor
        Sentence(16, 6, "I don't feel well.", "我不舒服。", "'feel' 注意長母音 /fiːl/", 1),
        Sentence(17, 6, "I have a headache.", "我頭痛。", "'headache' 是複合詞，重音在 'head'", 2),
        Sentence(18, 6, "Do I need a prescription?", "我需要處方嗎？", "'prescription' 重音在第二音節 /prɪˈskrɪpʃən/", 3),

        // 7. At the Supermarket
        Sentence(19, 7, "Where can I find the milk?", "牛奶在哪裡？", "'milk' 注意 /mɪlk/ 結尾的 /k/", 1),
        Sentence(20, 7, "How much does this cost?", "這個多少錢？", "'cost' 注意 /kɒst/ 的短母音", 2),
        Sentence(21, 7, "Can I pay by card?", "可以刷卡嗎？", "'pay' 注意 /peɪ/ 的雙母音", 3),

        // 8. Taking a Taxi
        Sentence(22, 8, "Can you take me to the airport?", "可以載我去機場嗎？", "'take me to' 三個詞連讀", 1),
        Sentence(23, 8, "How long will it take?", "要多久？", "'how long' 是常用問句型", 2),
        Sentence(24, 8, "Please stop here.", "請在這裡停。", "'stop' 注意 /stɒp/ 結尾爆破音", 3),

        // 9. At the Bank
        Sentence(25, 9, "I'd like to open an account.", "我想開一個帳戶。", "'account' 重音在第二音節 /əˈkaʊnt/", 1),
        Sentence(26, 9, "Can I exchange currency here?", "我可以在這裡換匯嗎？", "'currency' 重音在第一音節", 2),
        Sentence(27, 9, "What is the exchange rate?", "匯率是多少？", "'exchange rate' 重音在 'change' 和 'rate'", 3),

        // 10. Making a Phone Call
        Sentence(28, 10, "Hello, may I speak to...?", "你好，我可以找...嗎？", "'speak to' 連讀為 /spiːktə/", 1),
        Sentence(29, 10, "Can I leave a message?", "我可以留言嗎？", "'leave' 注意長母音 /liːv/", 2),
        Sentence(30, 10, "I'll call back later.", "我晚點再打來。", "'call back' 是片語動詞", 3),

        // 11. Ordering Coffee
        Sentence(31, 11, "I'd like a large latte, please.", "我要一杯大杯拿鐵，謝謝。", "'latte' 發音 /ˈlɑːteɪ/", 1),
        Sentence(32, 11, "Can I get that with oat milk?", "可以用燕麥奶嗎？", "'oat' 注意 /oʊt/ 的長母音", 2),
        Sentence(33, 11, "For here or to go?", "內用還是外帶？", "'to go' 美式表達外帶", 3),

        // 12. At the Post Office
        Sentence(34, 12, "I'd like to send this package.", "我想寄這個包裹。", "'package' 重音在第一音節 /ˈpækɪdʒ/", 1),
        Sentence(35, 12, "How long will it take to arrive?", "要多久才會到？", "'arrive' 重音在第二音節 /əˈraɪv/", 2),
        Sentence(36, 12, "I need to buy some stamps.", "我需要買一些郵票。", "'stamps' 注意 /stæmps/ 結尾子音群", 3),

        // 13. At the Gym
        Sentence(37, 13, "How do I use this machine?", "這台機器怎麼用？", "'machine' 重音在第二音節 /məˈʃiːn/", 1),
        Sentence(38, 13, "Where is the locker room?", "更衣室在哪裡？", "'locker' 注意 /ˈlɒkər/ 的短母音", 2),
        Sentence(39, 13, "Can I get a day pass?", "可以買一日券嗎？", "'day pass' 兩個詞都重讀", 3),

        // 14. Meeting New People
        Sentence(40, 14, "Hi, my name is... Nice to meet you!", "嗨，我叫...，很高興認識你！", "'Nice to meet you' 是固定用語，常連讀", 1),
        Sentence(41, 14, "Where are you from?", "你從哪裡來的？", "'where are you' 連讀為 /werɑːjə/", 2),
        Sentence(42, 14, "What do you do for a living?", "你做什麼工作？", "'for a living' 是固定搭配", 3),

        // 15. At the Library
        Sentence(43, 15, "Do you have this book?", "你們有這本書嗎？", "'book' 注意 /bʊk/ 的短母音", 1),
        Sentence(44, 15, "How long can I borrow it?", "我可以借多久？", "'borrow' 重音在第一音節 /ˈbɒroʊ/", 2),
        Sentence(45, 15, "Where is the reading room?", "閱覽室在哪裡？", "'reading' 注意 /ˈriːdɪŋ/ 的長母音", 3),

        // 16. Weather Talk
        Sentence(46, 16, "It's a beautiful day today!", "今天天氣真好！", "'beautiful' 重音在第一音節 /ˈbjuːtɪfəl/", 1),
        Sentence(47, 16, "Do you think it will rain?", "你覺得會下雨嗎？", "'rain' 注意 /reɪn/ 的雙母音", 2),
        Sentence(48, 16, "It's very cold outside.", "外面很冷。", "'cold' 注意 /koʊld/ 的長母音", 3),

        // 17. At the Movies
        Sentence(49, 17, "Two tickets for the seven o'clock show, please.", "請給我兩張七點場的票。", "'o'clock' 注意 /əˈklɒk/ 的連讀", 1),
        Sentence(50, 17, "Where are our seats?", "我們的座位在哪裡？", "'seats' 注意 /siːts/ 的長母音", 2),
        Sentence(51, 17, "Would you like some popcorn?", "你要爆米花嗎？", "'popcorn' 是複合詞，兩個音節都重讀", 3),

        // 18. Job Interview
        Sentence(52, 18, "Thank you for this opportunity.", "感謝這個機會。", "'opportunity' 重音在第三音節", 1),
        Sentence(53, 18, "I have three years of experience.", "我有三年的經驗。", "'experience' 重音在第二音節 /ɪkˈspɪriəns/", 2),
        Sentence(54, 18, "When can I start?", "我什麼時候可以開始？", "'when can I' 三個詞連讀", 3),

        // 19. At the Pharmacy
        Sentence(55, 19, "I need some cold medicine.", "我需要感冒藥。", "'medicine' 重音在第一音節 /ˈmedɪsɪn/", 1),
        Sentence(56, 19, "Do I need a prescription for this?", "這個需要處方嗎？", "'prescription' 注意 /prɪˈskrɪpʃən/", 2),
        Sentence(57, 19, "How often should I take this?", "這個多久吃一次？", "'how often' 是常用頻率問句", 3),

        // 20. Taking Public Transport
        Sentence(58, 20, "Which bus goes to downtown?", "哪班公車去市中心？", "'downtown' 重音在第二音節", 1),
        Sentence(59, 20, "How many stops to the museum?", "到博物館還有幾站？", "'museum' 重音在第二音節 /mjuˈziːəm/", 2),
        Sentence(60, 20, "Is this seat taken?", "這個位子有人坐嗎？", "'taken' 注意 /ˈteɪkən/ 的過去分詞", 3),

        // 21. At a Party
        Sentence(61, 21, "This is a great party!", "這個派對好棒！", "'great' 注意 /ɡreɪt/ 的雙母音", 1),
        Sentence(62, 21, "Would you like something to drink?", "你要喝點什麼嗎？", "'would you like' 是禮貌問句", 2),
        Sentence(63, 21, "I'm having a wonderful time.", "我玩得很開心。", "'wonderful' 重音在第一音節", 3),

        // 22. Booking a Reservation
        Sentence(64, 22, "I'd like to make a reservation.", "我想訂位。", "'reservation' 有四個音節，重音在第三", 1),
        Sentence(65, 22, "For what date and time?", "什麼日期和時間？", "'date' 和 'time' 都重讀", 2),
        Sentence(66, 22, "Can I cancel my reservation?", "我可以取消預訂嗎？", "'cancel' 重音在第一音節 /ˈkænsəl/", 3),

        // 23. Returning an Item
        Sentence(67, 23, "I'd like to return this item.", "我想退這個商品。", "'return' 重音在第二音節 /rɪˈtɜːrn/", 1),
        Sentence(68, 23, "Do you have the receipt?", "你有收據嗎？", "'receipt' 注意 'p' 不發音 /rɪˈsiːt/", 2),
        Sentence(69, 23, "Can I get a refund?", "可以退款嗎？", "'refund' 當名詞時重音在第一音節", 3),

        // 24. At the Hair Salon
        Sentence(70, 24, "I'd like a haircut, please.", "我想剪頭髮，謝謝。", "'haircut' 是複合詞，重音在 'hair'", 1),
        Sentence(71, 24, "Just a trim, please.", "只要修剪就好，謝謝。", "'trim' 注意 /trɪm/ 的短母音", 2),
        Sentence(72, 24, "I'd like it shorter on the sides.", "兩邊我想剪短一點。", "'shorter' 是比較級 /ˈʃɔːrtər/", 3),

        // 25. Talking About Hobbies
        Sentence(73, 25, "What do you like to do for fun?", "你平常喜歡做什麼？", "'for fun' 連讀", 1),
        Sentence(74, 25, "I enjoy reading and cooking.", "我喜歡閱讀和烹飪。", "'enjoy' 後接動名詞 (V-ing)", 2),
        Sentence(75, 25, "Do you play any sports?", "你有做什麼運動嗎？", "'sports' 注意 /spɔːrts/ 結尾子音群", 3),

        // 26. At the Gas Station
        Sentence(76, 26, "Fill it up, please.", "請加滿。", "'fill it up' 三詞連讀", 1),
        Sentence(77, 26, "Where is the restroom?", "洗手間在哪裡？", "'restroom' 是美式用法", 2),
        Sentence(78, 26, "How much per gallon?", "一加侖多少錢？", "'gallon' 重音在第一音節 /ˈɡælən/", 3),

        // 27. Emergency Situations
        Sentence(79, 27, "Please call an ambulance!", "請叫救護車！", "'ambulance' 重音在第一音節 /ˈæmbjʊləns/", 1),
        Sentence(80, 27, "I need help!", "我需要幫忙！", "'help' 注意 /help/ 簡短有力", 2),
        Sentence(81, 27, "Where is the nearest hospital?", "最近的醫院在哪裡？", "'nearest' 最高級 /ˈnɪrɪst/", 3),

        // 28. At the Beach
        Sentence(82, 28, "Let's go for a swim!", "我們去游泳吧！", "'swim' 注意 /swɪm/ 的 /sw/ 組合", 1),
        Sentence(83, 28, "Can I rent a surfboard?", "可以租衝浪板嗎？", "'surfboard' 複合詞重音在 'surf'", 2),
        Sentence(84, 28, "The water is so warm!", "水好溫暖！", "'warm' 注意 /wɔːrm/ 的發音", 3),

        // 29. Visiting a Friend
        Sentence(85, 29, "Thanks for inviting me over!", "謝謝你邀請我過來！", "'inviting' 重音在第二音節", 1),
        Sentence(86, 29, "Your home is lovely.", "你家好漂亮。", "'lovely' 注意 /ˈlʌvli/ 的短母音", 2),
        Sentence(87, 29, "Can I bring anything?", "我需要帶什麼嗎？", "'bring' 和 'anything' 自然連讀", 3),

        // 30. At the Office
        Sentence(88, 30, "Could you send me that email?", "你可以寄那封電子郵件給我嗎？", "'could you' 連讀為 /kʊdʒuː/", 1),
        Sentence(89, 30, "When is the meeting?", "會議是什麼時候？", "'meeting' 注意 /ˈmiːtɪŋ/ 的長母音", 2),
        Sentence(90, 30, "I'll finish the report by Friday.", "我會在星期五前完成報告。", "'finish' 和 'Friday' 都以 /f/ 開頭", 3)
    )
}

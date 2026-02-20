package com.bear.englishlearning.domain.conversation

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ConversationEngine @Inject constructor() {

    data class ConversationTopic(
        val id: String,
        val title: String,
        val titleZh: String,
        val greeting: String,
        val greetingZh: String
    )

    private val topics = listOf(
        ConversationTopic(
            "cafe", "At the Cafe", "在咖啡廳",
            "Hi! Welcome to Bear Cafe. What can I get you today?",
            "嗨！歡迎來到 Bear 咖啡廳。今天想要什麼呢？"
        ),
        ConversationTopic(
            "hotel", "Hotel Check-in", "飯店入住",
            "Good evening! Welcome to our hotel. Do you have a reservation?",
            "晚安！歡迎光臨我們的飯店。您有預約嗎？"
        ),
        ConversationTopic(
            "shopping", "Shopping", "購物",
            "Hello! Welcome to our store. Are you looking for anything in particular?",
            "你好！歡迎來到我們的商店。您有在找什麼特別的東西嗎？"
        ),
        ConversationTopic(
            "restaurant", "At the Restaurant", "在餐廳",
            "Good evening! Welcome to our restaurant. Table for how many?",
            "晚安！歡迎光臨我們的餐廳。請問幾位？"
        ),
        ConversationTopic(
            "directions", "Asking for Directions", "問路",
            "Excuse me, you look a bit lost. Can I help you find something?",
            "不好意思，你看起來有點迷路了。需要我幫你找什麼嗎？"
        ),
        ConversationTopic(
            "doctor", "At the Doctor", "看醫生",
            "Hello! I am Dr. Bear. How are you feeling today?",
            "你好！我是熊醫生。你今天感覺怎麼樣？"
        ),
        ConversationTopic(
            "airport", "At the Airport", "在機場",
            "Good morning! May I see your passport and boarding pass, please?",
            "早安！請出示您的護照和登機證好嗎？"
        ),
        ConversationTopic(
            "daily", "Daily Chat", "日常聊天",
            "Hey! How is it going? Nice weather today, right?",
            "嘿！最近怎麼樣？今天天氣不錯，對吧？"
        )
    )

    private var currentTopic: ConversationTopic = topics.random()

    fun getRandomTopic(): ConversationTopic {
        currentTopic = topics[Random.nextInt(topics.size)]
        return currentTopic
    }

    fun setTopic(topicId: String) {
        currentTopic = topics.find { it.id == topicId } ?: topics.first()
    }

    fun getCurrentTopic(): ConversationTopic = currentTopic

    fun getAllTopics(): List<ConversationTopic> = topics

    fun generateReply(
        userInput: String,
        conversationHistory: List<String> = emptyList()
    ): Pair<String, String> {
        val input = userInput.lowercase().trim()
        val turnCount = conversationHistory.size

        if (isGreeting(input)) return getGreetingReply()
        if (isGoodbye(input)) return getGoodbyeReply()
        if (isThankYou(input)) return getThankYouReply()
        if (isYes(input)) return getYesFollowUp()
        if (isNo(input)) return getNoFollowUp()
        if (isQuestion(input)) return getQuestionReply(input)
        return getTopicReply(input, turnCount)
    }

    private fun isGreeting(input: String): Boolean {
        val words = listOf(
            "hello", "hi", "hey", "good morning",
            "good afternoon", "good evening", "how are you"
        )
        return words.any { input.contains(it) }
    }

    private fun isGoodbye(input: String): Boolean {
        val words = listOf(
            "bye", "goodbye", "see you", "take care",
            "have a nice day", "good night", "gotta go"
        )
        return words.any { input.contains(it) }
    }

    private fun isThankYou(input: String): Boolean {
        val words = listOf("thank", "thanks", "appreciate", "grateful")
        return words.any { input.contains(it) }
    }

    private fun isYes(input: String): Boolean {
        val words = listOf(
            "yes", "yeah", "yep", "sure", "of course",
            "definitely", "absolutely", "okay", "ok"
        )
        return words.any { w -> input == w || input.startsWith(w + " ") }
    }

    private fun isNo(input: String): Boolean {
        val words = listOf("no", "nope", "not really", "nah")
        return words.any { w -> input == w || input.startsWith(w + " ") }
    }

    private fun isQuestion(input: String): Boolean {
        val starters = listOf(
            "what", "where", "when", "why", "how", "who",
            "which", "can", "could", "would", "do", "does", "is", "are"
        )
        return input.endsWith("?") || starters.any { input.startsWith(it) }
    }

    private fun getGreetingReply(): Pair<String, String> {
        return listOf(
            reply("Hello! Nice to meet you! How can I help you today?", "你好！很高興認識你！今天有什麼可以幫忙的嗎？"),
            reply("Hi there! Great to see you. What brings you here?", "嗨！很高興見到你。什麼風把你吹來的？"),
            reply("Hey! I am doing well, thanks. How about you?", "嘿！我很好，謝謝。你呢？"),
            reply("Good to see you! I hope you are having a wonderful day.", "很高興見到你！希望你今天過得很好。"),
            reply("Hi! Welcome! Is there anything I can do for you?", "嗨！歡迎！有什麼我可以為你效勞的嗎？")
        ).random()
    }

    private fun getGoodbyeReply(): Pair<String, String> {
        return listOf(
            reply("Goodbye! It was nice talking to you. Have a great day!", "再見！很高興和你聊天。祝你有美好的一天！"),
            reply("See you later! Take care and come back anytime!", "再見！保重，隨時歡迎回來！"),
            reply("Bye! I hope you enjoyed our conversation. See you next time!", "再見！希望你喜歡我們的對話。下次見！"),
            reply("Take care! It was a pleasure chatting with you.", "保重！很高興和你聊天。"),
            reply("Goodbye! Have a wonderful rest of your day!", "再見！祝你接下來的一天過得愉快！")
        ).random()
    }

    private fun getThankYouReply(): Pair<String, String> {
        return listOf(
            reply("You are welcome! Happy to help anytime.", "不客氣！隨時樂意幫忙。"),
            reply("No problem at all! Let me know if you need anything else.", "完全沒問題！如果還需要什麼就告訴我。"),
            reply("My pleasure! Is there anything else I can do for you?", "我的榮幸！還有其他我能幫忙的嗎？"),
            reply("Anytime! I am glad I could help.", "隨時！很高興能幫上忙。")
        ).random()
    }

    private fun getYesFollowUp(): Pair<String, String> {
        val replies = mapOf(
            "cafe" to listOf(
                reply("Great choice! Would you like that for here or to go?", "好選擇！您要內用還是外帶？"),
                reply("Sure thing! What size would you like? Small, medium, or large?", "沒問題！您要什麼尺寸？小杯、中杯還是大杯？"),
                reply("Perfect! Should I add any milk or sugar?", "完美！需要加牛奶或糖嗎？")
            ),
            "hotel" to listOf(
                reply("Wonderful! Let me look up your reservation. What name is it under?", "太好了！讓我查一下您的預約。是用什麼名字訂的？"),
                reply("Great! Your room is ready. Here is your key card.", "太好了！您的房間已經準備好了。這是您的房卡。"),
                reply("Perfect! Would you like a room with a view?", "完美！您想要有景觀的房間嗎？")
            ),
            "shopping" to listOf(
                reply("Excellent! Let me show you our latest collection.", "太好了！讓我帶您看我們最新的系列。"),
                reply("Great! We have some amazing deals today. Want to see them?", "太好了！我們今天有一些很棒的優惠。要看看嗎？"),
                reply("Sure! What size do you wear? I will find the perfect fit.", "好的！您穿什麼尺碼？我幫您找到最合適的。")
            ),
            "restaurant" to listOf(
                reply("Excellent! Right this way, please. Here is the menu.", "太好了！這邊請。這是菜單。"),
                reply("Perfect! Tonight special is grilled salmon. Would you like to try it?", "完美！今晚的特餐是烤鮭魚。要試試看嗎？"),
                reply("Great! Can I start you off with something to drink?", "好的！先來點飲料嗎？")
            ),
            "directions" to listOf(
                reply("Sure! Just go straight for two blocks, then turn left.", "好的！直走兩個街區，然後左轉。"),
                reply("Of course! It is about a 10-minute walk from here.", "當然！從這裡走大約10分鐘。"),
                reply("No problem! Take the first right, and it will be on your left side.", "沒問題！第一個路口右轉，就在你的左手邊。")
            ),
            "doctor" to listOf(
                reply("I see. Can you tell me more about your symptoms?", "我了解了。能告訴我更多關於你的症狀嗎？"),
                reply("Alright. Let me check your temperature first.", "好的。讓我先量一下你的體溫。"),
                reply("Okay. Have you been taking any medication recently?", "好的。你最近有在吃什麼藥嗎？")
            ),
            "airport" to listOf(
                reply("Thank you. Everything looks good. Your gate is B12.", "謝謝。一切沒問題。您的登機門是B12。"),
                reply("Perfect. Would you like a window or aisle seat?", "完美。您想要靠窗還是靠走道的座位？"),
                reply("Great! Do you have any luggage to check in?", "好的！您有需要託運的行李嗎？")
            )
        )
        val defaults = listOf(
            reply("That is great to hear! Tell me more about it.", "很高興聽到！多告訴我一些。"),
            reply("Sounds good! What else is on your mind?", "聽起來不錯！還有什麼想聊的嗎？"),
            reply("Wonderful! I would love to hear more about that.", "太棒了！我很想聽更多。")
        )
        return (replies[currentTopic.id] ?: defaults).random()
    }

    private fun getNoFollowUp(): Pair<String, String> {
        val replies = mapOf(
            "cafe" to reply("No worries! Would you like to take a look at our menu first?", "沒關係！要先看看我們的菜單嗎？"),
            "hotel" to reply("No problem! We do have rooms available. Would you like to book one?", "沒問題！我們有空房。您想訂一間嗎？"),
            "shopping" to reply("That is fine! Feel free to browse around.", "沒關係！請隨意逛逛。"),
            "restaurant" to reply("No problem! Would you like a few more minutes to decide?", "沒問題！需要再多幾分鐘考慮嗎？"),
            "directions" to reply("Okay, no worries! Is there somewhere else you need to go?", "好的，沒關係！你是要去其他地方嗎？"),
            "doctor" to reply("Alright. Are there any other symptoms you have noticed?", "好的。你有注意到其他症狀嗎？"),
            "airport" to reply("Okay. Is there anything else I can help you with?", "好的。還有什麼需要幫忙的嗎？")
        )
        val default = reply("That is okay! Is there something else you would like to talk about?", "沒關係！有其他想聊的嗎？")
        return replies[currentTopic.id] ?: default
    }

    private fun getQuestionReply(input: String): Pair<String, String> {
        if (input.contains("how much") || input.contains("price") || input.contains("cost")) {
            return getPriceReply()
        }
        if (input.contains("what time") || input.contains("when") || input.contains("how long")) {
            return getTimeReply()
        }
        if (input.contains("where") || input.contains("location") || input.contains("find")) {
            return getLocationReply()
        }
        if (input.contains("recommend") || input.contains("suggest") || input.contains("best") || input.contains("popular")) {
            return getRecommendReply()
        }
        return listOf(
            reply("That is a really good question! Let me think about that.", "這是個很好的問題！讓我想一下。"),
            reply("Great question! I would say the best approach is to start simple.", "好問題！我覺得最好的方式是從簡單的開始。"),
            reply("Hmm, let me see... It depends on what you are looking for.", "嗯，讓我看看……取決於你在找什麼。"),
            reply("Interesting question! I would recommend a few different options.", "有趣的問題！我建議考慮幾個不同的選項。")
        ).random()
    }

    private fun getPriceReply(): Pair<String, String> {
        val replies = mapOf(
            "cafe" to reply("A regular coffee is 3.50 and a latte is 4.50. We also have pastries starting at 2 dollars.", "一般咖啡是3.50美元，拿鐵是4.50美元。我們也有2美元起的糕點。"),
            "hotel" to reply("Our standard room is 120 per night, and the deluxe suite is 250. Breakfast is included.", "我們的標準房每晚120美元，豪華套房250美元。含早餐。"),
            "shopping" to reply("This item is 29.99. But we have a buy-one-get-one-half-off promotion today!", "這件商品29.99美元。但我們今天有買一送半價的促銷活動！"),
            "restaurant" to reply("The main courses range from 15 to 30 dollars. Our lunch special is only 12.", "主菜價格從15美元到30美元不等。我們的午餐特餐只要12美元。")
        )
        val default = reply("That is a great question! Let me find out the details for you.", "好問題！讓我幫你查一下細節。")
        return replies[currentTopic.id] ?: default
    }

    private fun getTimeReply(): Pair<String, String> {
        val replies = mapOf(
            "cafe" to reply("We are open from 7 AM to 9 PM every day. Your order should be ready in about 5 minutes.", "我們每天早上7點到晚上9點營業。您的餐點大約5分鐘就好。"),
            "hotel" to reply("Check-in is at 3 PM and check-out is at 11 AM. The pool is open until 10 PM.", "入住時間是下午3點，退房是上午11點。游泳池開放到晚上10點。"),
            "restaurant" to reply("We are open for lunch from 11:30 to 2:30, and dinner from 5:30 to 10.", "我們午餐是11:30到2:30，晚餐是5:30到10點。"),
            "airport" to reply("Your flight departs at 3:45 PM. Boarding begins about 30 minutes before.", "您的航班下午3:45起飛。登機大約在起飛前30分鐘開始。"),
            "doctor" to reply("The doctor will be with you in about 10 minutes.", "醫生大約10分鐘後來看你。")
        )
        val default = reply("Good question! Let me check on that for you right away.", "好問題！讓我馬上幫你查一下。")
        return replies[currentTopic.id] ?: default
    }

    private fun getLocationReply(): Pair<String, String> {
        val replies = mapOf(
            "hotel" to reply("The elevator is down the hall on your right. The restaurant is on the ground floor.", "電梯在走廊右邊。餐廳在一樓。"),
            "shopping" to reply("That section is on the second floor, near the escalator.", "那個區域在二樓，手扶梯附近。"),
            "airport" to reply("The gate is in Terminal 2. Follow the signs for international departures.", "登機門在第二航廈。跟著國際出發的指示走。"),
            "directions" to reply("It is about two blocks down this street, on the corner.", "就在這條街往下走大約兩個街區的轉角。")
        )
        val default = reply("Let me help you! What exactly are you looking for?", "讓我幫你！你具體在找什麼？")
        return replies[currentTopic.id] ?: default
    }

    private fun getRecommendReply(): Pair<String, String> {
        val replies = mapOf(
            "cafe" to reply("I highly recommend our caramel latte. It is our most popular drink!", "我大力推薦我們的焦糖拿鐵。這是最受歡迎的飲料！"),
            "restaurant" to reply("Our chef special today is the grilled salmon with roasted vegetables. Delicious!", "我們主廚今天的特餐是烤鮭魚配烤蔬菜。非常好吃！"),
            "shopping" to reply("Our best-selling item this month is this jacket. Very comfortable and stylish!", "這個月最暢銷的是這件外套。非常舒適又時尚！")
        )
        val default = reply("Great question! I would personally recommend the most popular option.", "好問題！我個人建議最受歡迎的選項。")
        return replies[currentTopic.id] ?: default
    }

    private fun getTopicReply(input: String, turnCount: Int): Pair<String, String> {
        return when (currentTopic.id) {
            "cafe" -> getCafeReply(input)
            "hotel" -> getHotelReply(input)
            "shopping" -> getShoppingReply(input)
            "restaurant" -> getRestaurantReply(input)
            "directions" -> getDirectionsReply(input)
            "doctor" -> getDoctorReply(input)
            "airport" -> getAirportReply(input)
            else -> getDailyReply(input)
        }
    }

    private fun getCafeReply(input: String): Pair<String, String> {
        if (input.contains("coffee") || input.contains("latte") || input.contains("cappuccino")) {
            return reply("Great choice! We make our coffee with freshly roasted beans. Would you like any flavor shots?", "好選擇！我們的咖啡用的是現烘的咖啡豆。要加什麼口味的糖漿嗎？")
        }
        if (input.contains("tea") || input.contains("green tea")) {
            return reply("We have green tea, black tea, chamomile, and jasmine. Which one interests you?", "我們有綠茶、紅茶、洋甘菊茶和茉莉花茶。你對哪一種有興趣？")
        }
        if (input.contains("cake") || input.contains("pastry") || input.contains("muffin") || input.contains("food")) {
            return reply("Our chocolate cake is freshly baked today! We also have blueberry muffins.", "我們的巧克力蛋糕是今天剛烤好的！我們也有藍莓馬芬。")
        }
        if (input.contains("wifi") || input.contains("internet")) {
            return reply("Sure! Our WiFi password is bearcafe2024. Stay as long as you like!", "當然！我們的WiFi密碼是bearcafe2024。請隨意待多久都行！")
        }
        return reply("Sounds good! Can I also interest you in one of our freshly baked pastries?", "聽起來不錯！要不要也試試我們新鮮烤好的糕點？")
    }

    private fun getHotelReply(input: String): Pair<String, String> {
        if (input.contains("room") || input.contains("bed") || input.contains("suite")) {
            return reply("We have single, double, and luxury suites. All include free WiFi and breakfast.", "我們有單人房、雙人房和豪華套房。都含免費WiFi和早餐。")
        }
        if (input.contains("breakfast") || input.contains("food") || input.contains("eat")) {
            return reply("Breakfast is served from 6:30 to 10 AM on the first floor.", "早餐在一樓供應，時間是早上6:30到10:00。")
        }
        if (input.contains("pool") || input.contains("gym") || input.contains("spa")) {
            return reply("Our pool and fitness center are on the 3rd floor, open from 6 AM to 10 PM.", "游泳池和健身中心在3樓，早上6點到晚上10點開放。")
        }
        return reply("Is there anything else you need during your stay?", "住宿期間還有什麼需要的嗎？")
    }

    private fun getShoppingReply(input: String): Pair<String, String> {
        if (input.contains("try") || input.contains("fitting") || input.contains("size")) {
            return reply("Absolutely! The fitting rooms are right over there.", "當然！試衣間就在那邊。")
        }
        if (input.contains("discount") || input.contains("sale") || input.contains("deal")) {
            return reply("We are having a 20 percent off sale this week! Plus buy two get one free on selected items.", "我們這週有八折優惠。而且部分商品買二送一！")
        }
        if (input.contains("pay") || input.contains("card") || input.contains("cash")) {
            return reply("We accept cash, credit cards, and mobile payments. Would you like a bag?", "我們接受現金、信用卡和行動支付。需要袋子嗎？")
        }
        return reply("I think this would be a great choice! Want to try it on first?", "我覺得這個選擇很棒！要先試穿看看嗎？")
    }

    private fun getRestaurantReply(input: String): Pair<String, String> {
        if (input.contains("order") || input.contains("want") || input.contains("get") || input.contains("like")) {
            return reply("Excellent choice! I will put that order in right away. Anything to drink?", "好選擇！我馬上幫您下單。要搭配什麼飲料嗎？")
        }
        if (input.contains("vegetarian") || input.contains("vegan") || input.contains("allergy")) {
            return reply("We have several vegetarian options and can accommodate most allergies.", "我們有好幾道素食選擇，也能處理大部分的過敏問題。")
        }
        if (input.contains("bill") || input.contains("check") || input.contains("pay")) {
            return reply("Of course! I will bring the check right over. Did you enjoy your meal?", "當然！我馬上拿帳單過來。您今天的餐點還滿意嗎？")
        }
        if (input.contains("water") || input.contains("drink") || input.contains("wine") || input.contains("juice")) {
            return reply("We have still and sparkling water, fresh juices, and a great wine selection.", "我們有靜水和氣泡水、新鮮果汁，還有很好的葡萄酒選擇。")
        }
        return reply("Our kitchen will have it ready shortly. Can I get you a refill?", "我們的廚房很快就準備好。需要幫您續杯嗎？")
    }

    private fun getDirectionsReply(input: String): Pair<String, String> {
        if (input.contains("station") || input.contains("train") || input.contains("subway") || input.contains("bus")) {
            return reply("The nearest station is about a 5-minute walk. Go straight and you will see it on your left.", "最近的車站步行大約5分鐘。直走你會在左手邊看到它。")
        }
        if (input.contains("far") || input.contains("walk") || input.contains("distance")) {
            return reply("It is about a 15-minute walk. You could also take a taxi, about 5 minutes.", "從這裡走大約15分鐘。你也可以搭計程車，大約5分鐘。")
        }
        if (input.contains("lost") || input.contains("help") || input.contains("confused")) {
            return reply("No worries! Where are you trying to go? I will walk you through it.", "別擔心！你要去哪裡？我一步一步帶你。")
        }
        return reply("Let me help you! Which direction are you heading?", "讓我幫你！你要往哪個方向？")
    }

    private fun getDoctorReply(input: String): Pair<String, String> {
        if (input.contains("headache") || input.contains("head") || input.contains("pain") || input.contains("hurt")) {
            return reply("How long have you had the headache? Have you taken any pain medication?", "頭痛多久了？有吃什麼止痛藥嗎？")
        }
        if (input.contains("cold") || input.contains("cough") || input.contains("fever") || input.contains("flu")) {
            return reply("It sounds like a cold. I will prescribe some medicine. Make sure to rest and drink water.", "聽起來像感冒。我會開一些藥。記得要好好休息，多喝水。")
        }
        if (input.contains("tired") || input.contains("sleep") || input.contains("exhausted")) {
            return reply("Fatigue can be caused by many things. Are you getting enough sleep?", "疲勞可能由很多原因造成。你有睡夠嗎？")
        }
        if (input.contains("stomach") || input.contains("nausea")) {
            return reply("Have you eaten anything unusual recently? I will give you some medicine.", "你最近有吃什麼特別的東西嗎？我會開一些藥。")
        }
        return reply("Thank you for telling me. I would like to run a few tests to make sure everything is okay.", "謝謝你告訴我。我想做幾個檢查確認一切正常。")
    }

    private fun getAirportReply(input: String): Pair<String, String> {
        if (input.contains("luggage") || input.contains("bag") || input.contains("suitcase")) {
            return reply("You are allowed one carry-on and one checked bag. Each up to 23 kilograms.", "您可以攜帶一件手提行李和一件託運行李。每件上限23公斤。")
        }
        if (input.contains("gate") || input.contains("terminal") || input.contains("boarding")) {
            return reply("Your gate is B12 in Terminal 2. Boarding starts 30 minutes before departure.", "您的登機門在第二航廈B12。登機在起飛前30分鐘開始。")
        }
        if (input.contains("delay") || input.contains("cancel") || input.contains("late")) {
            return reply("Sorry for the inconvenience. The flight is delayed by about 45 minutes.", "很抱歉造成不便。航班延誤了大約45分鐘。")
        }
        if (input.contains("shop") || input.contains("store") || input.contains("buy")) {
            return reply("The duty-free shops are right after security, on the left side.", "免稅店就在安檢後的左側。")
        }
        return reply("Your flight information looks good. Anything else you need before boarding?", "您的航班資訊沒問題。登機前還有什麼需要的嗎？")
    }

    private fun getDailyReply(input: String): Pair<String, String> {
        if (input.contains("weather") || input.contains("sunny") || input.contains("rain")) {
            return reply("Yes! The weather has been really nice lately. Perfect for going outside.", "是啊！最近天氣真的很好。很適合出門。")
        }
        if (input.contains("hobby") || input.contains("free time") || input.contains("fun") || input.contains("weekend")) {
            return reply("That sounds fun! I enjoy reading and walking. What do you do on weekends?", "聽起來很好玩！我喜歡看書和散步。你週末都做什麼？")
        }
        if (input.contains("work") || input.contains("job") || input.contains("busy")) {
            return reply("Work can be stressful sometimes. It is important to take breaks.", "工作有時壓力很大。適當休息很重要。")
        }
        if (input.contains("food") || input.contains("eat") || input.contains("hungry") || input.contains("lunch")) {
            return reply("I love trying new food! Have you tried any good restaurants recently?", "我喜歡嘗試新食物！你最近有試過什麼好餐廳嗎？")
        }
        if (input.contains("movie") || input.contains("music") || input.contains("book") || input.contains("game")) {
            return reply("Oh, I love that topic! What kind do you enjoy? I am open to recommendations.", "噢，我喜歡這個話題！你喜歡什麼類型？隨時歡迎推薦。")
        }
        if (input.contains("travel") || input.contains("trip") || input.contains("vacation")) {
            return reply("Traveling is the best! Where would you love to visit next?", "旅行是最棒的！你接下來最想去哪裡？")
        }
        return listOf(
            reply("That is really interesting! Tell me more about that.", "真的很有趣！多告訴我一些。"),
            reply("I see! That is a great point. What do you think about it?", "我懂了！很好的觀點。你怎麼看？"),
            reply("Oh, cool! I would love to hear more.", "噢，真酷！我很想聽更多。"),
            reply("That makes sense! By the way, have you been practicing English?", "很有道理！順便問一下，你有定期練習英文嗎？"),
            reply("Nice! I enjoy chatting about these things. What else is new?", "很好！我喜歡聊這些。還有什麼新鮮事？")
        ).random()
    }

    private fun reply(en: String, zh: String): Pair<String, String> = Pair(en, zh)
}

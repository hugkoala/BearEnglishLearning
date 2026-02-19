package com.bear.englishlearning.domain.conversation

import com.bear.englishlearning.data.local.entity.Conversation
import com.bear.englishlearning.data.local.entity.ConversationLine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

data class GeneratedConversation(
    val conversation: Conversation,
    val lines: List<ConversationLine>
)

/**
 * Generates random conversations by randomly selecting a scenario template
 * and assembling dialogue lines from sentence pools with random variations.
 */
@Singleton
class RandomConversationGenerator @Inject constructor() {

    private val scenarios = listOf(
        ScenarioTemplate(
            title = "Ordering Food", titleZh = "點餐",
            lines = listOf(
                listOf(
                    DialoguePair("Hi, could I get a table for two?", "嗨，可以給我一張兩人桌嗎？", "'table for two' 是常用搭配"),
                    DialoguePair("Hello, I'd like a table by the window, please.", "你好，我想要靠窗的桌子。", "'by the window' 表示靠窗"),
                    DialoguePair("Good evening! Do you have any tables available?", "晚上好！還有空桌嗎？", "'available' 重音在第二音節 /əˈveɪləbəl/"),
                    DialoguePair("Hi there! Table for one, please.", "嗨！一人桌，謝謝。", "'table for one' 單人用餐表達")
                ),
                listOf(
                    DialoguePair("Of course! Right this way. Here's the menu.", "當然！這邊請。這是菜單。", "'right this way' 是服務業常用語"),
                    DialoguePair("Sure! Please follow me. Can I start you off with some drinks?", "好的！請跟我來。先來點飲料嗎？", "'start you off' 表示先從...開始"),
                    DialoguePair("Absolutely! Let me show you to your table.", "當然！我帶您到座位。", "'show you to' 表示帶某人到某處")
                ),
                listOf(
                    DialoguePair("What do you recommend?", "你推薦什麼？", "'recommend' 重音在第三音節 /ˌrɛkəˈmɛnd/"),
                    DialoguePair("What's the most popular dish here?", "這裡最受歡迎的菜是什麼？", "'popular' 重音在第一音節 /ˈpɒpjʊlər/"),
                    DialoguePair("Do you have any specials today?", "今天有什麼特餐嗎？", "'specials' 指今日特餐"),
                    DialoguePair("Can I see the dessert menu too?", "可以也看甜點菜單嗎？", "'dessert' 重音在第二音節 /dɪˈzɜːrt/")
                ),
                listOf(
                    DialoguePair("Our pasta is very popular! The steak is also great.", "我們的義大利麵很受歡迎！牛排也很棒。", "'pasta' 注意 /ˈpɑːstə/ 的發音"),
                    DialoguePair("I'd suggest the grilled salmon. It's our chef's special.", "我推薦烤鮭魚，這是主廚特餐。", "'salmon' 的 l 不發音 /ˈsæmən/"),
                    DialoguePair("The chicken soup is amazing, and the salad is fresh.", "雞湯很棒，沙拉也很新鮮。", "'amazing' 重音在第二音節 /əˈmeɪzɪŋ/")
                ),
                listOf(
                    DialoguePair("I'll have the pasta, please. And a glass of water.", "我要義大利麵，謝謝。再來一杯水。", "'a glass of' 是量詞搭配"),
                    DialoguePair("Sounds great! I'll take the salmon.", "聽起來不錯！我要鮭魚。", "'I'll take' 表示我要點..."),
                    DialoguePair("The steak sounds good. Medium rare, please.", "牛排聽起來不錯。五分熟，謝謝。", "'medium rare' 是牛排熟度")
                ),
                listOf(
                    DialoguePair("Excellent choice! I'll put that order in right away.", "很好的選擇！我馬上幫您下單。", "'right away' 表示立刻"),
                    DialoguePair("Great! Your food will be ready in about fifteen minutes.", "好的！餐點大約十五分鐘就好。", "'in about' 表示大約多久後"),
                    DialoguePair("Coming right up! Would you like anything else?", "馬上來！還需要其他的嗎？", "'coming right up' 表示馬上送來")
                ),
                listOf(
                    DialoguePair("This is delicious! Can I get the bill, please?", "太好吃了！可以買單嗎？", "'delicious' 重音在第二音節 /dɪˈlɪʃəs/"),
                    DialoguePair("Everything was wonderful. Check, please!", "一切都很棒。買單，謝謝！", "'check' 在美式英語中表示帳單"),
                    DialoguePair("That was amazing! Could we have the receipt?", "太棒了！可以給我收據嗎？", "'receipt' 的 p 不發音 /rɪˈsiːt/")
                ),
                listOf(
                    DialoguePair("Here you go! Thank you for dining with us.", "給您！感謝您來用餐。", "'dining' 是 'dine' 的動名詞"),
                    DialoguePair("Sure! The total is thirty-five dollars. Have a great night!", "好的！總共三十五美元。祝您有美好的夜晚！", "'total' 重音在第一音節 /ˈtoʊtəl/"),
                    DialoguePair("Of course! We hope to see you again soon.", "當然！希望很快再見到您。", "'hope to' 表示希望...")
                )
            )
        ),
        ScenarioTemplate(
            title = "Shopping", titleZh = "購物",
            lines = listOf(
                listOf(
                    DialoguePair("Hi, I'm looking for a birthday gift. Can you help me?", "嗨，我在找生日禮物。可以幫我嗎？", "'looking for' 表示尋找"),
                    DialoguePair("Excuse me, do you have this jacket in a different color?", "不好意思，這件外套有其他顏色嗎？", "'different' 重音在第一音節 /ˈdɪfərənt/"),
                    DialoguePair("Hello! I need a new pair of shoes.", "你好！我需要一雙新鞋子。", "'a pair of' 是量詞搭配")
                ),
                listOf(
                    DialoguePair("Of course! What kind of gift are you thinking of?", "當然！你在想什麼樣的禮物呢？", "'thinking of' 後接名詞或動名詞"),
                    DialoguePair("Let me check! We have it in black and red.", "我查看一下！有黑色和紅色的。", "'let me check' 表示讓我看看"),
                    DialoguePair("Sure! What style are you looking for?", "當然！您想要什麼風格的？", "'style' 注意 /staɪl/ 的雙母音")
                ),
                listOf(
                    DialoguePair("Maybe something practical? Like a scarf or a wallet.", "也許實用的東西？像是圍巾或錢包。", "'practical' 重音在第一音節 /ˈpræktɪkəl/"),
                    DialoguePair("How much is this one? And is it on sale?", "這個多少錢？有打折嗎？", "'on sale' 表示打折中"),
                    DialoguePair("Something comfortable for everyday use.", "日常穿著舒適的就好。", "'comfortable' 重音在第一音節 /ˈkʌmfərtəbəl/")
                ),
                listOf(
                    DialoguePair("This leather wallet is very popular. It's forty dollars.", "這個皮革錢包很受歡迎。四十美元。", "'leather' 注意 /ˈlɛðər/ 的 th 發音"),
                    DialoguePair("It's sixty dollars, but we have 20% off today!", "六十美元，不過今天打八折！", "'percent off' 表示折扣"),
                    DialoguePair("These sneakers are really comfortable. Want to try them on?", "這雙運動鞋真的很舒適。要試穿嗎？", "'sneakers' 美式用法指運動鞋")
                ),
                listOf(
                    DialoguePair("That looks nice! Can I see it up close?", "看起來不錯！可以近看嗎？", "'up close' 表示近距離"),
                    DialoguePair("Yes, please! Do you have my size?", "好的，謝謝！有我的尺寸嗎？", "'my size' 指適合的尺碼"),
                    DialoguePair("I love it! I'll take it. Can you gift-wrap it?", "我喜歡！我要了。可以包裝成禮物嗎？", "'gift-wrap' 是複合動詞表示禮物包裝")
                ),
                listOf(
                    DialoguePair("Absolutely! Would you like me to wrap it?", "當然！需要幫你包裝嗎？", "'wrap' 注意 /ræp/ 的 w 不影響發音"),
                    DialoguePair("We have sizes 7 through 11. Let me grab your size.", "我們有 7 到 11 號。我幫你拿。", "'grab' 表示快速拿取"),
                    DialoguePair("Sure thing! Cash or card?", "沒問題！現金還是刷卡？", "'sure thing' 是口語的「當然」")
                ),
                listOf(
                    DialoguePair("Yes, please! I'll pay by card.", "好的，謝謝！我用卡付。", "'pay by card' 是付款方式表達"),
                    DialoguePair("They fit perfectly! I'll take them.", "完美合腳！我要了。", "'perfectly' 注意 /ˈpɜːrfɪktli/ 的發音"),
                    DialoguePair("Card, please. Do you have a bag?", "刷卡，謝謝。有袋子嗎？", "'bag' 注意 /bæɡ/ 短母音")
                ),
                listOf(
                    DialoguePair("Here's your receipt. Thank you for shopping with us!", "這是您的收據。感謝您來購物！", "'shopping with us' 是服務業結尾用語"),
                    DialoguePair("Great choice! Come back anytime.", "很好的選擇！歡迎隨時再來。", "'anytime' 表示任何時候"),
                    DialoguePair("Here you are! Have a wonderful day.", "給您！祝您有美好的一天。", "'have a wonderful day' 是禮貌結尾")
                )
            )
        ),
        ScenarioTemplate(
            title = "At the Doctor", titleZh = "看醫生",
            lines = listOf(
                listOf(
                    DialoguePair("Good morning, doctor. I haven't been feeling well lately.", "早安，醫生。我最近身體不太舒服。", "'haven't been feeling' 是現在完成進行式"),
                    DialoguePair("Doctor, I've had a terrible cough for a week.", "醫生，我咳嗽了一個禮拜。", "'terrible' 重音在第一音節 /ˈtɛrɪbəl/"),
                    DialoguePair("Hi, doctor. My stomach has been hurting since yesterday.", "嗨，醫生。我的肚子從昨天開始痛。", "'stomach' 注意 /ˈstʌmək/ 的發音")
                ),
                listOf(
                    DialoguePair("I see. Can you tell me more about your symptoms?", "了解。你可以多描述一下症狀嗎？", "'symptoms' 重音在第一音節 /ˈsɪmptəmz/"),
                    DialoguePair("Let's take a look. How long have you had this?", "我來看看。這個狀況多久了？", "'how long' 是常用問句型"),
                    DialoguePair("I'm sorry to hear that. When did it start?", "很遺憾聽到。什麼時候開始的？", "'when did it start' 是問時間起點")
                ),
                listOf(
                    DialoguePair("I've had a sore throat, headache, and I feel very tired.", "我喉嚨痛、頭痛，而且覺得很累。", "'sore throat' 重音在 'sore'"),
                    DialoguePair("It gets worse at night, and I can't sleep well.", "晚上更嚴重，我睡不好。", "'gets worse' 表示惡化"),
                    DialoguePair("I also feel nauseous and have no appetite.", "我也覺得噁心，沒有食慾。", "'nauseous' 注意 /ˈnɔːziəs/ 的發音")
                ),
                listOf(
                    DialoguePair("Let me check your temperature and blood pressure.", "讓我量一下你的體溫和血壓。", "'temperature' 重音在第一音節 /ˈtɛmprɪtʃər/"),
                    DialoguePair("I'd like to do a quick examination. Please open your mouth.", "我做個簡單檢查。請張開嘴巴。", "'examination' 重音在第四音節"),
                    DialoguePair("Please sit on the examination table. I'll listen to your chest.", "請坐到檢查台上。我聽一下你的胸腔。", "'chest' 注意 /tʃɛst/ 的發音")
                ),
                listOf(
                    DialoguePair("Is it anything serious?", "嚴重嗎？", "'serious' 重音在第一音節 /ˈsɪriəs/"),
                    DialoguePair("Do I need to get any tests done?", "我需要做什麼檢查嗎？", "'get tests done' 表示做檢查"),
                    DialoguePair("Should I be worried?", "我需要擔心嗎？", "'worried' 注意 /ˈwʌrid/ 的短母音")
                ),
                listOf(
                    DialoguePair("It looks like you have the flu. Nothing too serious.", "看起來你得了流感，不算太嚴重。", "'flu' 注意 /fluː/ 的長母音"),
                    DialoguePair("Don't worry. It's a common cold. You just need some rest.", "別擔心。只是普通感冒。你需要休息。", "'common cold' 是常見搭配"),
                    DialoguePair("Your blood pressure is slightly high. Let me prescribe some medicine.", "你的血壓稍微偏高。讓我開點藥。", "'prescribe' 重音在第二音節 /prɪˈskraɪb/")
                ),
                listOf(
                    DialoguePair("What medicine should I take? And how often?", "我應該吃什麼藥？多久吃一次？", "'how often' 問頻率"),
                    DialoguePair("Thank you, doctor. Is there anything else I should do?", "謝謝醫生。還有什麼我該注意的嗎？", "'anything else' 表示其他事項"),
                    DialoguePair("How long will it take to recover?", "大概多久會好？", "'recover' 重音在第二音節 /rɪˈkʌvər/")
                ),
                listOf(
                    DialoguePair("Take this medicine twice a day after meals. Drink lots of water and get plenty of rest.", "飯後一天吃兩次這個藥。多喝水，多休息。", "'twice a day' 表示一天兩次"),
                    DialoguePair("Rest for a few days and avoid cold drinks. Come back if it doesn't improve.", "休息幾天，避免冷飲。如果沒好轉就回來看。", "'improve' 重音在第二音節 /ɪmˈpruːv/"),
                    DialoguePair("You should feel better in about a week. Take it easy!", "大約一週就會好很多。放輕鬆！", "'take it easy' 是常用表達表示放鬆")
                )
            )
        ),
        ScenarioTemplate(
            title = "Hotel Check-In", titleZh = "飯店入住",
            lines = listOf(
                listOf(
                    DialoguePair("Good evening! I have a reservation under the name Chen.", "晚安！我有預訂，名字是陳。", "'under the name' 是預訂的固定表達"),
                    DialoguePair("Hi, I booked a room online. My confirmation number is 12345.", "嗨，我在網路上訂了房間。確認號是 12345。", "'confirmation number' 是訂房確認碼"),
                    DialoguePair("Hello! I'd like to check in, please.", "你好！我想辦理入住。", "'check in' 是片語動詞")
                ),
                listOf(
                    DialoguePair("Welcome! Let me pull up your reservation. May I see your ID?", "歡迎！讓我調出您的預訂資料。可以看一下證件嗎？", "'pull up' 表示調出、查詢"),
                    DialoguePair("Of course! One moment, please. How many nights will you be staying?", "好的！請稍等。您要住幾晚？", "'how many nights' 是入住常見問句"),
                    DialoguePair("Welcome to our hotel! Could I see your passport, please?", "歡迎來到我們的飯店！可以看您的護照嗎？", "'passport' 重音在第一音節 /ˈpæspɔːrt/")
                ),
                listOf(
                    DialoguePair("Here you go. I'm staying for three nights.", "給你。我要住三晚。", "'three nights' 注意 /θriː naɪts/"),
                    DialoguePair("Sure, here's my passport. Is the room ready?", "好，這是我的護照。房間準備好了嗎？", "'ready' 重音在第一音節 /ˈrɛdi/"),
                    DialoguePair("Here it is. Also, is breakfast included?", "在這裡。另外，有含早餐嗎？", "'included' 注意 /ɪnˈkluːdɪd/ 三音節")
                ),
                listOf(
                    DialoguePair("Perfect! You're in room 608 on the sixth floor. Here's your key card.", "好的！您在六樓 608 號房。這是您的房卡。", "'key card' 是常見搭配"),
                    DialoguePair("Yes, everything is ready! Your room is on the eighth floor.", "是的，都準備好了！您的房間在八樓。", "'eighth' 注意 /eɪtθ/ 的發音"),
                    DialoguePair("Breakfast is from 7 to 10 AM in the lobby restaurant.", "早餐在大廳餐廳，早上 7 點到 10 點。", "'lobby' 重音在第一音節 /ˈlɒbi/")
                ),
                listOf(
                    DialoguePair("Great, thank you! What time is check-out?", "太好了，謝謝！退房時間是幾點？", "'check-out' 重音在 'check'"),
                    DialoguePair("Is there Wi-Fi in the room? What's the password?", "房間有 Wi-Fi 嗎？密碼是什麼？", "'password' 重音在第一音節"),
                    DialoguePair("Wonderful! Can I request a wake-up call at 7 AM?", "太好了！可以安排早上七點的叫醒服務嗎？", "'wake-up call' 是飯店叫醒服務")
                ),
                listOf(
                    DialoguePair("Check-out is at noon. The elevators are to your left.", "退房時間是中午。電梯在你左邊。", "'elevators' 重音在第一音節 /ˈɛlɪveɪtərz/"),
                    DialoguePair("Yes! The Wi-Fi name is 'HotelGuest' and the password is on this card.", "有！Wi-Fi 名稱是 'HotelGuest'，密碼在這張卡上。", "'guest' 注意 /ɡɛst/ 的發音"),
                    DialoguePair("Absolutely! I'll set that up for you. Anything else?", "當然！我幫您安排。還需要什麼嗎？", "'set that up' 表示安排好")
                ),
                listOf(
                    DialoguePair("Is there a gym or pool in the hotel?", "飯店有健身房或游泳池嗎？", "'gym' 注意 /dʒɪm/ 的發音"),
                    DialoguePair("One more thing — can I get extra towels?", "還有一件事 — 可以多給我毛巾嗎？", "'extra' 重音在第一音節 /ˈɛkstrə/"),
                    DialoguePair("That's all I need. Thank you so much!", "這就是我需要的了。非常感謝！", "'that's all' 表示就這些了")
                ),
                listOf(
                    DialoguePair("Yes! The pool is on the rooftop, open until 9 PM. Enjoy your stay!", "有！泳池在頂樓，開放到晚上九點。祝您住宿愉快！", "'rooftop' 是複合詞重音在 'roof'"),
                    DialoguePair("Of course! I'll have housekeeping bring some up. Welcome again!", "當然！我請房務部送一些上去。再次歡迎您！", "'housekeeping' 是飯店房務部"),
                    DialoguePair("You're welcome! Don't hesitate to call the front desk if you need anything.", "不客氣！如需任何東西，請隨時打電話到櫃台。", "'front desk' 是飯店前台/櫃台")
                )
            )
        ),
        ScenarioTemplate(
            title = "Asking for Directions", titleZh = "問路",
            lines = listOf(
                listOf(
                    DialoguePair("Excuse me, could you tell me how to get to the train station?", "不好意思，可以告訴我怎麼去火車站嗎？", "'could you tell me' 是禮貌問路方式"),
                    DialoguePair("Hi, I'm lost. Is the shopping mall near here?", "嗨，我迷路了。購物中心在附近嗎？", "'I'm lost' 表示我迷路了"),
                    DialoguePair("Sorry to bother you. Which way to the museum?", "不好意思打擾了。博物館往哪個方向走？", "'which way' 是問方向的常用方式")
                ),
                listOf(
                    DialoguePair("Sure! Go straight along this road for about two blocks.", "當然！沿著這條路直走大約兩個街區。", "'go straight' 表示直走"),
                    DialoguePair("Of course! It's not too far. Walk down this street and turn right.", "沒問題！不太遠。沿這條街走然後右轉。", "'turn right' 是方向指示常用語"),
                    DialoguePair("No problem! You need to take the second left.", "沒問題！你要在第二個路口左轉。", "'second left' 表示第二個左轉路口")
                ),
                listOf(
                    DialoguePair("And then? Do I turn at the traffic light?", "然後呢？在紅綠燈那裡轉嗎？", "'traffic light' 重音在 'traffic'"),
                    DialoguePair("Is it on this side of the street or the other?", "是在這邊還是對面？", "'this side' 和 'the other' 形成對比"),
                    DialoguePair("Got it. Is there a landmark I should look for?", "了解。有什麼地標可以找嗎？", "'landmark' 是複合詞重音在 'land'")
                ),
                listOf(
                    DialoguePair("Yes! Turn right at the big intersection. You'll see it on your left.", "對！在大路口右轉。你會在左邊看到它。", "'intersection' 重音在第三音節"),
                    DialoguePair("It's on the left side, right next to the park.", "在左邊，就在公園旁邊。", "'right next to' 表示正旁邊"),
                    DialoguePair("Look for the big red building. It's right across from it.", "找那棟大紅色建築。就在對面。", "'right across from' 表示正對面")
                ),
                listOf(
                    DialoguePair("How long does it take to walk there?", "走路過去要多久？", "'how long does it take' 是問時間的常用句"),
                    DialoguePair("Is it walkable, or should I take a bus?", "走得到嗎？還是我應該搭公車？", "'walkable' 表示步行可達"),
                    DialoguePair("Can I get there by subway?", "我可以搭地鐵去嗎？", "'get there' 連讀為 /ɡɛtðɛr/")
                ),
                listOf(
                    DialoguePair("About ten minutes on foot. It's a nice walk!", "走路大約十分鐘。沿路風景很好！", "'on foot' 是固定搭配表示步行"),
                    DialoguePair("It's pretty close. Maybe five minutes from here.", "蠻近的，從這裡大約五分鐘。", "'pretty close' 表示蠻近的"),
                    DialoguePair("You could walk, but taking the bus is faster. Bus number 12 goes there.", "你可以走路，但搭公車比較快。12 號公車到那裡。", "'bus number' 搭配數字表示幾號公車")
                ),
                listOf(
                    DialoguePair("Perfect! Thank you so much for your help!", "太好了！非常感謝你的幫忙！", "'thank you so much' 表達強烈感謝"),
                    DialoguePair("Great, I think I can find it now. Thanks a lot!", "太好了，我想我現在找得到了。非常感謝！", "'thanks a lot' 是口語感謝方式"),
                    DialoguePair("Got it! You've been really helpful. Thanks!", "了解！你真的很有幫助。謝謝！", "'helpful' 重音在第一音節 /ˈhɛlpfəl/")
                ),
                listOf(
                    DialoguePair("You're welcome! Enjoy your visit.", "不客氣！祝你參觀愉快。", "'enjoy' 重音在第二音節 /ɪnˈdʒɔɪ/"),
                    DialoguePair("No problem! If you get lost again, just ask anyone around here.", "沒問題！如果又迷路了，隨便問附近的人就好。", "'get lost' 表示迷路"),
                    DialoguePair("Happy to help! Have a great day.", "很樂意幫忙！祝你有美好的一天。", "'happy to help' 是常用回應")
                )
            )
        ),
        ScenarioTemplate(
            title = "At the Coffee Shop", titleZh = "在咖啡廳",
            lines = listOf(
                listOf(
                    DialoguePair("Hi! What can I get for you today?", "嗨！今天想喝什麼？", "'what can I get for you' 是點餐常用問句"),
                    DialoguePair("Welcome! Ready to order?", "歡迎！準備好點餐了嗎？", "'ready to' 後接原形動詞"),
                    DialoguePair("Good morning! Take your time with the menu.", "早安！慢慢看菜單。", "'take your time' 表示不急")
                ),
                listOf(
                    DialoguePair("I'd like a large iced latte, please.", "我要一杯大杯冰拿鐵，謝謝。", "'iced latte' 重音在 'iced'"),
                    DialoguePair("Can I get a hot cappuccino? And maybe a muffin?", "可以給我一杯熱卡布奇諾嗎？也許再來一個馬芬。", "'cappuccino' 重音在第三音節 /ˌkæpʊˈtʃiːnoʊ/"),
                    DialoguePair("Just a black coffee, no sugar. Medium size, please.", "一杯黑咖啡就好，不加糖。中杯，謝謝。", "'no sugar' 簡潔表達不要加糖")
                ),
                listOf(
                    DialoguePair("Would you like regular milk or oat milk?", "你要一般牛奶還是燕麥奶？", "'regular' 重音在第一音節 /ˈrɛɡjʊlər/"),
                    DialoguePair("Great choices! For here or to go?", "很好的選擇！內用還是外帶？", "'for here or to go' 是美式咖啡店用語"),
                    DialoguePair("Sure thing! Anything else with that?", "沒問題！還要其他的嗎？", "'anything else' 表示還有其他嗎")
                ),
                listOf(
                    DialoguePair("Oat milk, please. And can I get less ice?", "燕麥奶，謝謝。可以少冰嗎？", "'less ice' 表示少冰"),
                    DialoguePair("For here, please. And a chocolate croissant!", "內用，謝謝。再來一個巧克力可頌！", "'croissant' 注意法語發音 /krwɑːˈsɒ̃/"),
                    DialoguePair("Actually, could I also add a sandwich?", "其實，可以再加一個三明治嗎？", "'actually' 表示其實/事實上")
                ),
                listOf(
                    DialoguePair("Of course! Your total is six fifty.", "當然！總共六塊五。", "'total' 重音在第一音節 /ˈtoʊtəl/"),
                    DialoguePair("That comes to nine dollars. Cash or card?", "這樣是九美元。現金還是刷卡？", "'that comes to' 表示總計"),
                    DialoguePair("Coming right up! That'll be seven twenty-five.", "馬上來！七塊二十五。", "'coming right up' 表示馬上準備")
                ),
                listOf(
                    DialoguePair("Card, please. Do you have any loyalty rewards?", "刷卡，謝謝。你們有集點嗎？", "'loyalty rewards' 是會員獎勵方案"),
                    DialoguePair("Here you go! Keep the change.", "給你！不用找了。", "'keep the change' 表示不用找零"),
                    DialoguePair("I'll pay by card. Thanks!", "我用卡付。謝謝！", "'pay by card' 表示刷卡付款")
                ),
                listOf(
                    DialoguePair("Your latte is ready! Enjoy.", "你的拿鐵好了！請享用。", "'enjoy' 重音在第二音節 /ɪnˈdʒɔɪ/"),
                    DialoguePair("Here's your order! Have a great day.", "這是您的餐點！祝您有美好的一天。", "'here's your order' 是取餐用語"),
                    DialoguePair("Order number seven! Your coffee is ready.", "七號！您的咖啡好了。", "'order number' 是叫號取餐用語")
                ),
                listOf(
                    DialoguePair("Thank you! This place is really cozy. I'll definitely come back.", "謝謝！這個地方好溫馨。我一定會再來。", "'cozy' 注意 /ˈkoʊzi/ 的長母音"),
                    DialoguePair("Thanks! The coffee smells amazing.", "謝謝！咖啡聞起來超棒的。", "'smells amazing' 用感官動詞+形容詞"),
                    DialoguePair("Perfect! See you next time.", "完美！下次見。", "'see you next time' 是道別常用語")
                )
            )
        ),
        ScenarioTemplate(
            title = "Taking a Taxi", titleZh = "搭計程車",
            lines = listOf(
                listOf(
                    DialoguePair("Hi! Could you take me to the airport, please?", "嗨！可以載我去機場嗎？", "'take me to' 連讀"),
                    DialoguePair("Hello! I need to get to Central Station as quickly as possible.", "你好！我需要盡快到中央車站。", "'as quickly as possible' 表示盡快"),
                    DialoguePair("Excuse me, are you available? I need a ride to the hospital.", "不好意思，你有空嗎？我需要到醫院。", "'available' 重音在第二音節 /əˈveɪləbəl/")
                ),
                listOf(
                    DialoguePair("Sure, hop in! Do you have a preferred route?", "好，上車！你有偏好的路線嗎？", "'hop in' 是口語表示上車"),
                    DialoguePair("Of course! It'll take about twenty minutes with traffic.", "沒問題！有車潮的話大約二十分鐘。", "'with traffic' 表示考慮交通狀況"),
                    DialoguePair("No problem! Let me turn on the meter.", "沒問題！我開表。", "'turn on' 表示開啟")
                ),
                listOf(
                    DialoguePair("Please take the fastest route. I'm running late.", "請走最快的路。我快遲到了。", "'running late' 表示快要遲到"),
                    DialoguePair("Is there a lot of traffic this time of day?", "這個時間交通狀況如何？", "'this time of day' 表示一天中的這個時候"),
                    DialoguePair("Can you avoid the highway? I'd prefer local roads.", "可以避開高速公路嗎？我偏好一般道路。", "'avoid' 重音在第二音節 /əˈvɔɪd/")
                ),
                listOf(
                    DialoguePair("Got it! I'll take the expressway. It should be faster right now.", "了解！我走快速道路。現在應該比較快。", "'expressway' 重音在第二音節"),
                    DialoguePair("There's some traffic on Main Street, but I know a shortcut.", "主街有些塞車，但我知道一條捷徑。", "'shortcut' 是複合詞重音在 'short'"),
                    DialoguePair("Sure! The side streets should be clear at this hour.", "好！這個時間小路應該暢通。", "'clear' 表示暢通無阻")
                ),
                listOf(
                    DialoguePair("Actually, can we make a quick stop at the convenience store?", "其實，可以在便利商店快速停一下嗎？", "'make a stop' 表示停留一下"),
                    DialoguePair("How much will the fare be approximately?", "車費大概多少？", "'approximately' 重音在第二音節 /əˈprɒksɪmɪtli/"),
                    DialoguePair("Great, thanks! Could you turn up the air conditioning?", "太好了，謝謝！可以把冷氣調大嗎？", "'turn up' 表示調大")
                ),
                listOf(
                    DialoguePair("No problem! It'll just be a minute.", "沒問題！只要一分鐘就好。", "'just a minute' 是常用表達"),
                    DialoguePair("It'll probably be around twenty to twenty-five dollars.", "大概二十到二十五美元左右。", "'around' 表示大約"),
                    DialoguePair("Sure thing! Is the temperature okay now?", "沒問題！溫度可以了嗎？", "'temperature' 重音在第一音節 /ˈtɛmprɪtʃər/")
                ),
                listOf(
                    DialoguePair("We're here! That'll be eighteen fifty.", "到了！十八塊五。", "'we're here' 表示我們到了"),
                    DialoguePair("Here we are! The total on the meter is twenty-two dollars.", "到了！跳表顯示二十二美元。", "'here we are' 表示到了"),
                    DialoguePair("This is the place. That comes to fifteen dollars.", "就是這裡。總共十五美元。", "'this is the place' 表示目的地到了")
                ),
                listOf(
                    DialoguePair("Here you go. Keep the change! Thank you.", "給你。不用找了！謝謝。", "'keep the change' 表示不用找零"),
                    DialoguePair("Thanks for the ride! Could I get a receipt?", "謝謝載我！可以給我收據嗎？", "'thanks for the ride' 是搭車後的感謝"),
                    DialoguePair("Great, thanks! You've been really helpful. Have a good day!", "太好了，謝謝！你真的很幫忙。祝你有美好的一天！", "'have a good day' 是日常道別用語")
                )
            )
        ),
        ScenarioTemplate(
            title = "Making Friends", titleZh = "交朋友",
            lines = listOf(
                listOf(
                    DialoguePair("Hi there! I don't think we've met. I'm Alex.", "嗨！我想我們沒見過。我是 Alex。", "'I don't think we've met' 是自我介紹開場白"),
                    DialoguePair("Hey! You look familiar. Are you in Professor Lee's class?", "嘿！你看起來很面熟。你在李教授的課上嗎？", "'look familiar' 表示看起來面熟"),
                    DialoguePair("Hi! Is this seat taken? Mind if I sit here?", "嗨！這個位子有人坐嗎？我可以坐嗎？", "'mind if I' 是禮貌詢問的常用句")
                ),
                listOf(
                    DialoguePair("Nice to meet you, Alex! I'm Jamie. Are you new here?", "很高興認識你，Alex！我是 Jamie。你是新來的嗎？", "'nice to meet you' 是見面固定用語"),
                    DialoguePair("Yes, I am! I just transferred this semester. I'm Sam.", "是的！我這學期剛轉來。我是 Sam。", "'transferred' 注意 /trænsˈfɜːrd/ 過去式"),
                    DialoguePair("Go ahead! I'm Taylor. I was just studying for the exam.", "請坐！我是 Taylor。我正在準備考試。", "'go ahead' 表示請便")
                ),
                listOf(
                    DialoguePair("Yeah, I just moved here from Taipei. Still getting used to things.", "對，我剛從台北搬來。還在適應中。", "'getting used to' 表示逐漸適應"),
                    DialoguePair("That's cool! What are you majoring in?", "好酷！你主修什麼？", "'majoring in' 表示主修"),
                    DialoguePair("Same here! That exam is going to be tough. Want to study together?", "我也是！那個考試會很難。要一起讀書嗎？", "'tough' 注意 /tʌf/ 的發音")
                ),
                listOf(
                    DialoguePair("I'm studying computer science. How about you?", "我讀資工。你呢？", "'computer science' 是常見科系名"),
                    DialoguePair("I'd love that! I'm better at memorizing when I study with someone.", "好啊！我跟人一起讀比較容易記住。", "'memorizing' 重音在第一音節 /ˈmɛməraɪzɪŋ/"),
                    DialoguePair("I'm still exploring, but I'm leaning towards business.", "我還在探索，但傾向商學。", "'leaning towards' 表示傾向")
                ),
                listOf(
                    DialoguePair("Nice! Do you like it so far? What do you do for fun?", "不錯！到目前為止喜歡嗎？你平常做什麼消遣？", "'so far' 表示到目前為止"),
                    DialoguePair("I love cooking and watching movies. What about your hobbies?", "我喜歡煮飯和看電影。你的興趣呢？", "'what about' 是反問常用方式"),
                    DialoguePair("Cool! I'm actually part of the photography club. You should join!", "好酷！我其實是攝影社的。你應該加入！", "'part of' 表示...的一員")
                ),
                listOf(
                    DialoguePair("I'm really into hiking and photography! We should hang out sometime.", "我很迷健行和攝影！我們應該找時間出去玩。", "'hang out' 表示一起消磨時間"),
                    DialoguePair("That sounds like fun! I've always wanted to try photography.", "聽起來很有趣！我一直想試試攝影。", "'I've always wanted to' 表示一直想要"),
                    DialoguePair("I'd love to! When do you guys meet?", "我很想！你們什麼時候聚會？", "'you guys' 是口語的「你們」")
                ),
                listOf(
                    DialoguePair("Definitely! Let's exchange numbers. I'll text you!", "一定！我們交換號碼吧。我傳訊息給你！", "'exchange numbers' 表示交換電話號碼"),
                    DialoguePair("Here's my phone number. Feel free to message me anytime!", "這是我的電話號碼。隨時傳訊息給我！", "'feel free to' 表示不用客氣"),
                    DialoguePair("Every Wednesday evening! I'll send you the details.", "每週三晚上！我傳詳情給你。", "'details' 重音在第一音節 /ˈdiːteɪlz/")
                ),
                listOf(
                    DialoguePair("Awesome! It was really nice meeting you. See you around!", "太棒了！真的很高興認識你。到時見！", "'see you around' 表示改天見"),
                    DialoguePair("Same here! Looking forward to hanging out. Bye!", "我也是！期待一起出去玩。再見！", "'looking forward to' 後接名詞或動名詞"),
                    DialoguePair("Thanks! I'm really glad we met today. Catch you later!", "謝謝！我真的很高興今天認識你。下次見！", "'catch you later' 是口語道別方式")
                )
            )
        ),
        ScenarioTemplate(
            title = "At the Supermarket", titleZh = "在超市",
            lines = listOf(
                listOf(
                    DialoguePair("Excuse me, where can I find the cooking oil?", "不好意思，食用油在哪裡？", "'cooking oil' 是常見搭配"),
                    DialoguePair("Hi, do you know if this yogurt is dairy-free?", "嗨，你知道這個優格是無乳製品的嗎？", "'dairy-free' 表示無乳製品"),
                    DialoguePair("Could you help me? I can't reach the top shelf.", "可以幫我嗎？我搆不到最上面的架子。", "'reach' 注意 /riːtʃ/ 的長母音")
                ),
                listOf(
                    DialoguePair("It's in aisle four, next to the vinegar.", "在第四走道，醋的旁邊。", "'aisle' 注意 s 不發音 /aɪl/"),
                    DialoguePair("Let me check the label for you... Yes, it's plant-based!", "我幫你看標籤...是的，是植物性的！", "'plant-based' 表示植物性的"),
                    DialoguePair("Of course! Here you go. Is this the one you wanted?", "當然！給你。是你要的這個嗎？", "'here you go' 表示給你")
                ),
                listOf(
                    DialoguePair("Thanks! Also, are there any deals on fruits today?", "謝謝！另外，今天水果有特價嗎？", "'deals' 表示特價優惠"),
                    DialoguePair("Great, thank you! Where's the organic section?", "太好了，謝謝！有機食品區在哪？", "'organic section' 是超市分區"),
                    DialoguePair("Yes, that's the one! Do you have any shopping bags?", "是的，就是這個！你們有購物袋嗎？", "'shopping bags' 是常見搭配")
                ),
                listOf(
                    DialoguePair("Strawberries are buy one get one free! They're very fresh.", "草莓買一送一！非常新鮮。", "'buy one get one free' 是常見促銷方式"),
                    DialoguePair("It's right at the back of the store, near the dairy section.", "就在店的最後面，乳製品區附近。", "'at the back of' 表示在後面"),
                    DialoguePair("We have paper bags for free, or reusable bags for two dollars.", "我們有免費的紙袋，或兩美元的環保袋。", "'reusable' 重音在第二音節 /riːˈjuːzəbəl/")
                ),
                listOf(
                    DialoguePair("I'll take two boxes of strawberries then! Where do I pay?", "那我要兩盒草莓！哪裡結帳？", "'two boxes of' 是量詞搭配"),
                    DialoguePair("Perfect! I also need some eggs and bread.", "太好了！我還需要一些雞蛋和麵包。", "'eggs and bread' 是日常食材"),
                    DialoguePair("A paper bag is fine. Can I also use a self-checkout?", "紙袋就好。我可以用自助結帳嗎？", "'self-checkout' 是自助結帳機")
                ),
                listOf(
                    DialoguePair("The checkout is at the front. You can also use self-checkout on the right.", "結帳在前面。你也可以用右邊的自助結帳。", "'on the right' 表示在右邊"),
                    DialoguePair("Eggs are in aisle two, and bread is near the entrance.", "雞蛋在第二走道，麵包在入口附近。", "'entrance' 重音在第一音節 /ˈɛntrəns/"),
                    DialoguePair("Of course! The self-checkout machines are right over there.", "當然！自助結帳機就在那邊。", "'right over there' 表示就在那裡")
                ),
                listOf(
                    DialoguePair("Great, thanks for all your help!", "太好了，謝謝你的幫忙！", "'thanks for all your help' 是表達感謝的完整句"),
                    DialoguePair("Found them! Okay, I think I have everything I need.", "找到了！好，我想我需要的都買齊了。", "'everything I need' 表示所需的一切"),
                    DialoguePair("Perfect! This store has such a great selection.", "太好了！這家店的商品選擇真的很好。", "'selection' 重音在第二音節 /sɪˈlɛkʃən/")
                ),
                listOf(
                    DialoguePair("You're welcome! Come again anytime.", "不客氣！歡迎隨時再來。", "'come again' 表示歡迎再來"),
                    DialoguePair("Glad I could help! Have a nice day.", "很高興能幫到忙！祝您愉快。", "'glad I could help' 是服務業常用語"),
                    DialoguePair("Thanks! We get new stock every morning. See you!", "謝謝！我們每天早上進新貨。再見！", "'new stock' 表示新貨品")
                )
            )
        ),
        ScenarioTemplate(
            title = "At the Gym", titleZh = "在健身房",
            lines = listOf(
                listOf(
                    DialoguePair("Hey! Is this your first time at this gym?", "嘿！你是第一次來這家健身房嗎？", "'first time' 是常見搭配"),
                    DialoguePair("Excuse me, are you using this machine?", "不好意思，你在用這台機器嗎？", "'using' 注意 /ˈjuːzɪŋ/ 的發音"),
                    DialoguePair("Hi! Do you know how to adjust this treadmill?", "嗨！你知道怎麼調整這台跑步機嗎？", "'treadmill' 是複合詞重音在 'tread'")
                ),
                listOf(
                    DialoguePair("Yeah, I just signed up yesterday! Still figuring things out.", "對，我昨天剛報名！還在搞清楚狀況。", "'figuring out' 表示搞清楚"),
                    DialoguePair("No, go ahead! I just finished my set.", "沒有，請用！我剛做完一組。", "'finished my set' 表示完成一組動作"),
                    DialoguePair("Sure! Press this button to change the speed.", "當然！按這個按鈕改變速度。", "'press' 注意 /prɛs/ 的發音")
                ),
                listOf(
                    DialoguePair("Nice! What's your workout routine like?", "不錯！你的健身計畫是什麼？", "'workout routine' 是健身常用搭配"),
                    DialoguePair("Thanks! How many sets do you usually do?", "謝謝！你通常做幾組？", "'how many sets' 是健身常見問句"),
                    DialoguePair("Got it, thanks! How fast do you usually run?", "了解，謝謝！你通常跑多快？", "'how fast' 問速度")
                ),
                listOf(
                    DialoguePair("I usually do cardio first, then some weight training.", "我通常先做有氧，然後再做重訓。", "'cardio' 是 cardiovascular 的縮寫 /ˈkɑːrdioʊ/"),
                    DialoguePair("Three to four sets of twelve reps. Then I stretch.", "三到四組，每組十二下。然後拉伸。", "'reps' 是 repetitions 的縮寫"),
                    DialoguePair("I start slow at 6 km/h and increase to 10. Want to try?", "我從時速 6 公里慢慢開始，增加到 10。要試試嗎？", "'increase' 當動詞重音在第二音節 /ɪnˈkriːs/")
                ),
                listOf(
                    DialoguePair("That sounds like a good plan! I'm trying to build muscle.", "聽起來是個好計畫！我想增肌。", "'build muscle' 表示增肌"),
                    DialoguePair("Wow, that's intense! I'm just a beginner.", "哇，好厲害！我只是新手。", "'beginner' 重音在第二音節 /bɪˈɡɪnər/"),
                    DialoguePair("Sure! I've never tried running on a treadmill before.", "好啊！我以前沒在跑步機上跑過。", "'never tried' 表示從未嘗試過")
                ),
                listOf(
                    DialoguePair("You should try the bench press! Start with lighter weights.", "你應該試臥推！先從輕的重量開始。", "'bench press' 是重訓動作 /bɛntʃ prɛs/"),
                    DialoguePair("Everyone starts somewhere! Consistency is key.", "每個人都是從零開始的！持之以恆是關鍵。", "'consistency' 重音在第二音節 /kənˈsɪstənsi/"),
                    DialoguePair("Don't worry! Start at a comfortable pace and work your way up.", "不用擔心！從舒適的速度開始慢慢增加。", "'work your way up' 表示逐步提升")
                ),
                listOf(
                    DialoguePair("Thanks for the tips! I really appreciate it.", "謝謝你的建議！我真的很感激。", "'appreciate' 重音在第二音節 /əˈpriːʃieɪt/"),
                    DialoguePair("You're right! Maybe we can be workout buddies?", "你說得對！也許我們可以一起健身？", "'workout buddies' 表示健身夥伴"),
                    DialoguePair("Great advice! I feel more confident now.", "很好的建議！我現在更有信心了。", "'confident' 重音在第一音節 /ˈkɒnfɪdənt/")
                ),
                listOf(
                    DialoguePair("Anytime! Good luck with your training. See you around!", "隨時！祝你訓練順利。到時見！", "'good luck with' 表示祝...順利"),
                    DialoguePair("Absolutely! I'm here every Monday, Wednesday, and Friday.", "當然！我每週一、三、五都在這裡。", "'every Monday' 表示每個星期一"),
                    DialoguePair("You've got this! Remember, progress takes time.", "你一定可以的！記住，進步需要時間。", "'you've got this' 是鼓勵語")
                )
            )
        )
    )

    /**
     * Generate a random conversation. Returns a Conversation + ConversationLines
     * with negative IDs so they don't clash with database-seeded conversations.
     */
    fun generate(): GeneratedConversation {
        val template = scenarios.random()
        val conversationId = -(Random.nextLong(1, 100000))

        val conversation = Conversation(
            conversationId = conversationId,
            title = template.title,
            titleZh = template.titleZh,
            scenarioTag = "random"
        )

        val lines = template.lines.mapIndexed { index, pool ->
            val chosen = pool.random()
            val speaker = if (index % 2 == 0) "A" else "B"
            ConversationLine(
                lineId = conversationId * 100 - index.toLong(),
                conversationId = conversationId,
                speaker = speaker,
                englishText = chosen.english,
                chineseText = chosen.chinese,
                pronunciationTip = chosen.tip,
                orderIndex = index + 1
            )
        }

        return GeneratedConversation(conversation, lines)
    }
}

private data class DialoguePair(
    val english: String,
    val chinese: String,
    val tip: String
)

private data class ScenarioTemplate(
    val title: String,
    val titleZh: String,
    val lines: List<List<DialoguePair>>
)

package com.bear.englishlearning.domain.vocabulary

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Generates a daily vocabulary sheet of 10 words.
 * Uses the current date as seed so the same words appear all day,
 * but a new set appears each day.
 */
@Singleton
class DailyVocabularyGenerator @Inject constructor() {

    companion object {
        private const val DAILY_WORD_COUNT = 10
    }

    fun generateForToday(): List<VocabularyWord> {
        return generateForDate(LocalDate.now())
    }

    fun generateForDate(date: LocalDate): List<VocabularyWord> {
        val seed = date.toEpochDay()
        val random = Random(seed)
        val allWords = getAllWords()
        return allWords.shuffled(random).take(DAILY_WORD_COUNT)
    }

    private fun getAllWords(): List<VocabularyWord> = listOf(
        // ====== Daily Life ======
        VocabularyWord("appointment", "n.", "/əˈpɔɪntmənt/", "a scheduled meeting or arrangement", "預約；約定", "I have a dentist appointment at 3 PM.", "我下午三點有牙醫的預約。", "Daily Life"),
        VocabularyWord("available", "adj.", "/əˈveɪləbəl/", "able to be used or obtained", "可用的；有空的", "Is this table available?", "這張桌子有空嗎？", "Daily Life"),
        VocabularyWord("comfortable", "adj.", "/ˈkʌmfərtəbəl/", "providing physical ease and relaxation", "舒適的", "These shoes are really comfortable.", "這雙鞋真的很舒適。", "Daily Life"),
        VocabularyWord("convenient", "adj.", "/kənˈviːniənt/", "fitting in well with needs or plans", "方便的", "The store is in a very convenient location.", "這家店的位置很方便。", "Daily Life"),
        VocabularyWord("delicious", "adj.", "/dɪˈlɪʃəs/", "highly pleasant to the taste", "美味的", "This pasta is absolutely delicious!", "這義大利麵真是太好吃了！", "Daily Life"),
        VocabularyWord("expensive", "adj.", "/ɪkˈspɛnsɪv/", "costing a lot of money", "昂貴的", "This restaurant is too expensive for me.", "這家餐廳對我來說太貴了。", "Daily Life"),
        VocabularyWord("grocery", "n.", "/ˈɡroʊsəri/", "food and supplies sold in a store", "雜貨；食品", "I need to buy some groceries after work.", "我下班後需要買一些食品雜貨。", "Daily Life"),
        VocabularyWord("ingredient", "n.", "/ɪnˈɡriːdiənt/", "any of the foods used to make a dish", "食材；成分", "What are the main ingredients in this soup?", "這碗湯的主要食材是什麼？", "Daily Life"),
        VocabularyWord("neighbor", "n.", "/ˈneɪbər/", "a person living near another", "鄰居", "My neighbor is very friendly and helpful.", "我的鄰居非常友善且樂於助人。", "Daily Life"),
        VocabularyWord("organize", "v.", "/ˈɔːrɡənaɪz/", "to arrange systematically", "組織；整理", "I need to organize my closet this weekend.", "我這個週末需要整理我的衣櫥。", "Daily Life"),
        VocabularyWord("purchase", "v./n.", "/ˈpɜːrtʃəs/", "to buy something", "購買", "I'd like to purchase two tickets, please.", "我想買兩張票，謝謝。", "Daily Life"),
        VocabularyWord("receipt", "n.", "/rɪˈsiːt/", "a written record of a purchase", "收據", "Could I have the receipt, please?", "可以給我收據嗎？", "Daily Life"),
        VocabularyWord("schedule", "n./v.", "/ˈskɛdʒuːl/", "a plan of times for activities", "時間表；安排", "Let me check my schedule for next week.", "讓我看看下週的行程。", "Daily Life"),
        VocabularyWord("temperature", "n.", "/ˈtɛmprɪtʃər/", "the degree of heat or cold", "溫度", "The temperature will drop below zero tonight.", "今晚溫度會降到零度以下。", "Daily Life"),
        VocabularyWord("umbrella", "n.", "/ʌmˈbrɛlə/", "a device used for protection from rain", "雨傘", "Don't forget to bring your umbrella today.", "今天別忘了帶雨傘。", "Daily Life"),

        // ====== Travel ======
        VocabularyWord("accommodation", "n.", "/əˌkɒməˈdeɪʃən/", "a place to stay", "住宿", "We booked accommodation near the beach.", "我們訂了海灘附近的住宿。", "Travel"),
        VocabularyWord("boarding", "n.", "/ˈbɔːrdɪŋ/", "the act of getting on a plane or ship", "登機；登船", "Boarding begins in thirty minutes.", "三十分鐘後開始登機。", "Travel"),
        VocabularyWord("currency", "n.", "/ˈkɜːrənsi/", "the money used in a country", "貨幣", "What currency do they use in Japan?", "日本使用什麼貨幣？", "Travel"),
        VocabularyWord("departure", "n.", "/dɪˈpɑːrtʃər/", "the act of leaving", "出發；離開", "Our departure time has been changed to 5 PM.", "我們的出發時間改為下午五點。", "Travel"),
        VocabularyWord("destination", "n.", "/ˌdɛstɪˈneɪʃən/", "the place to which someone is going", "目的地", "What's your final destination?", "你的最終目的地是哪裡？", "Travel"),
        VocabularyWord("itinerary", "n.", "/aɪˈtɪnəreri/", "a planned route or journey", "行程表", "Our itinerary includes three cities in Europe.", "我們的行程包含歐洲三個城市。", "Travel"),
        VocabularyWord("luggage", "n.", "/ˈlʌɡɪdʒ/", "bags and suitcases for traveling", "行李", "How many pieces of luggage can I bring?", "我可以帶幾件行李？", "Travel"),
        VocabularyWord("passport", "n.", "/ˈpæspɔːrt/", "an official travel document", "護照", "Make sure your passport is valid before traveling.", "旅行前確認你的護照是否有效。", "Travel"),
        VocabularyWord("reservation", "n.", "/ˌrɛzərˈveɪʃən/", "an arrangement to keep a place", "預訂", "I made a reservation at the hotel for three nights.", "我在飯店預訂了三晚。", "Travel"),
        VocabularyWord("souvenir", "n.", "/ˌsuːvəˈnɪr/", "something kept as a reminder of a place", "紀念品", "I bought a small souvenir for my friend.", "我給朋友買了一個小紀念品。", "Travel"),
        VocabularyWord("transfer", "v./n.", "/ˈtrænsfɜːr/", "to move from one place to another", "轉乘；轉帳", "We need to transfer at the next station.", "我們需要在下一站轉車。", "Travel"),
        VocabularyWord("vacation", "n.", "/veɪˈkeɪʃən/", "a period of rest from work", "假期", "I'm planning a vacation to Hawaii.", "我正在計畫去夏威夷度假。", "Travel"),

        // ====== Food & Dining ======
        VocabularyWord("appetizer", "n.", "/ˈæpɪtaɪzər/", "a small dish served before a meal", "開胃菜", "Would you like to start with an appetizer?", "你想先來點開胃菜嗎？", "Food"),
        VocabularyWord("beverage", "n.", "/ˈbɛvərɪdʒ/", "a drink of any type", "飲料", "What beverage would you like with your meal?", "你的餐點要搭配什麼飲料？", "Food"),
        VocabularyWord("cuisine", "n.", "/kwɪˈziːn/", "a style of cooking", "料理；菜系", "Italian cuisine is popular all over the world.", "義大利料理在全世界都很受歡迎。", "Food"),
        VocabularyWord("dessert", "n.", "/dɪˈzɜːrt/", "a sweet course served after a meal", "甜點", "Would you like to see the dessert menu?", "你想看甜點菜單嗎？", "Food"),
        VocabularyWord("flavor", "n.", "/ˈfleɪvər/", "the taste of food or drink", "味道；風味", "This ice cream comes in many different flavors.", "這冰淇淋有很多不同的口味。", "Food"),
        VocabularyWord("portion", "n.", "/ˈpɔːrʃən/", "an amount of food served to one person", "份量", "The portions here are very generous.", "這裡的份量非常大方。", "Food"),
        VocabularyWord("recommend", "v.", "/ˌrɛkəˈmɛnd/", "to suggest as good or suitable", "推薦", "Can you recommend a good restaurant nearby?", "你能推薦附近的好餐廳嗎？", "Food"),
        VocabularyWord("spicy", "adj.", "/ˈspaɪsi/", "having a strong hot flavor", "辣的", "I can't eat anything too spicy.", "我不能吃太辣的東西。", "Food"),
        VocabularyWord("vegetarian", "adj./n.", "/ˌvɛdʒɪˈtɛriən/", "not eating meat", "素食的；素食者", "Do you have any vegetarian options?", "你們有素食的選項嗎？", "Food"),

        // ====== Health ======
        VocabularyWord("allergy", "n.", "/ˈælədʒi/", "a harmful reaction to a substance", "過敏", "I have a serious allergy to peanuts.", "我對花生嚴重過敏。", "Health"),
        VocabularyWord("bandage", "n.", "/ˈbændɪdʒ/", "a strip of material for wrapping a wound", "繃帶", "The nurse put a bandage on my knee.", "護士在我的膝蓋上貼了繃帶。", "Health"),
        VocabularyWord("cough", "n./v.", "/kɒf/", "to expel air from the lungs noisily", "咳嗽", "I've had a bad cough for a week.", "我咳嗽了一個禮拜。", "Health"),
        VocabularyWord("diagnosis", "n.", "/ˌdaɪəɡˈnoʊsɪs/", "the identification of a disease", "診斷", "The doctor gave me a diagnosis of the flu.", "醫生診斷我得了流感。", "Health"),
        VocabularyWord("exercise", "n./v.", "/ˈɛksərsaɪz/", "physical activity for health", "運動；鍛鍊", "Regular exercise is important for your health.", "規律運動對你的健康很重要。", "Health"),
        VocabularyWord("fever", "n.", "/ˈfiːvər/", "an abnormally high body temperature", "發燒", "She stayed home because she had a fever.", "她因為發燒待在家裡。", "Health"),
        VocabularyWord("headache", "n.", "/ˈhɛdeɪk/", "a continuous pain in the head", "頭痛", "I woke up with a terrible headache.", "我醒來時頭痛得很厲害。", "Health"),
        VocabularyWord("medicine", "n.", "/ˈmɛdɪsɪn/", "a substance used to treat illness", "藥物", "Remember to take your medicine after meals.", "記得飯後吃藥。", "Health"),
        VocabularyWord("pharmacy", "n.", "/ˈfɑːrməsi/", "a store that sells medicines", "藥局", "Is there a pharmacy near here?", "附近有藥局嗎？", "Health"),
        VocabularyWord("prescription", "n.", "/prɪˈskrɪpʃən/", "a doctor's order for medicine", "處方", "You need a prescription to buy this medicine.", "你需要處方才能買這個藥。", "Health"),
        VocabularyWord("symptom", "n.", "/ˈsɪmptəm/", "a sign of illness", "症狀", "What symptoms do you have?", "你有什麼症狀？", "Health"),

        // ====== Work & Business ======
        VocabularyWord("colleague", "n.", "/ˈkɒliːɡ/", "a person you work with", "同事", "My colleague helped me finish the report.", "我的同事幫我完成了報告。", "Work"),
        VocabularyWord("deadline", "n.", "/ˈdɛdlaɪn/", "the latest time something must be done", "截止日期", "The deadline for the project is next Friday.", "這個專案的截止日是下週五。", "Work"),
        VocabularyWord("experience", "n.", "/ɪkˈspɪriəns/", "knowledge gained from doing something", "經驗", "I have three years of experience in marketing.", "我有三年的行銷經驗。", "Work"),
        VocabularyWord("interview", "n./v.", "/ˈɪntərvjuː/", "a formal meeting to assess a candidate", "面試；訪問", "I have a job interview tomorrow morning.", "我明天早上有一個工作面試。", "Work"),
        VocabularyWord("opportunity", "n.", "/ˌɒpərˈtuːnɪti/", "a chance for advancement", "機會", "This is a great opportunity to learn new skills.", "這是學習新技能的好機會。", "Work"),
        VocabularyWord("presentation", "n.", "/ˌprɛzənˈteɪʃən/", "a formal talk to an audience", "簡報；報告", "She gave an excellent presentation at the meeting.", "她在會議上做了一場出色的簡報。", "Work"),
        VocabularyWord("professional", "adj./n.", "/prəˈfɛʃənəl/", "relating to a job requiring skill", "專業的；專業人士", "He is very professional in his work.", "他工作非常專業。", "Work"),
        VocabularyWord("promotion", "n.", "/prəˈmoʊʃən/", "advancement to a higher position", "升遷；促銷", "She got a promotion to senior manager.", "她升遷為資深經理。", "Work"),
        VocabularyWord("salary", "n.", "/ˈsæləri/", "a fixed regular payment for work", "薪水", "What is the starting salary for this position?", "這個職位的起薪是多少？", "Work"),
        VocabularyWord("supervisor", "n.", "/ˈsuːpərvaɪzər/", "a person who oversees others' work", "主管", "I need to ask my supervisor for permission.", "我需要問我的主管是否允許。", "Work"),

        // ====== Emotions & Descriptions ======
        VocabularyWord("anxious", "adj.", "/ˈæŋkʃəs/", "feeling worried or nervous", "焦慮的", "I feel anxious about the exam tomorrow.", "我對明天的考試感到焦慮。", "Emotions"),
        VocabularyWord("confident", "adj.", "/ˈkɒnfɪdənt/", "feeling sure of oneself", "有自信的", "She felt confident after preparing well.", "她準備充分後感到很有自信。", "Emotions"),
        VocabularyWord("curious", "adj.", "/ˈkjʊriəs/", "eager to know or learn", "好奇的", "The children were curious about the new teacher.", "孩子們對新老師很好奇。", "Emotions"),
        VocabularyWord("disappointed", "adj.", "/ˌdɪsəˈpɔɪntɪd/", "sad because expectations were not met", "失望的", "I was disappointed that the concert was canceled.", "演唱會取消了，我很失望。", "Emotions"),
        VocabularyWord("embarrassed", "adj.", "/ɪmˈbærəst/", "feeling ashamed or awkward", "尷尬的", "I was so embarrassed when I forgot her name.", "當我忘記她的名字時，我非常尷尬。", "Emotions"),
        VocabularyWord("exhausted", "adj.", "/ɪɡˈzɔːstɪd/", "extremely tired", "精疲力竭的", "After the long hike, I was completely exhausted.", "長途健行後，我完全精疲力竭。", "Emotions"),
        VocabularyWord("frustrated", "adj.", "/ˈfrʌstreɪtɪd/", "feeling upset because of inability to change something", "沮喪的；挫敗的", "He felt frustrated because the computer kept crashing.", "他很沮喪因為電腦一直當機。", "Emotions"),
        VocabularyWord("generous", "adj.", "/ˈdʒɛnərəs/", "willing to give freely", "慷慨的", "She is always generous with her time.", "她總是很慷慨地付出她的時間。", "Emotions"),
        VocabularyWord("grateful", "adj.", "/ˈɡreɪtfəl/", "feeling thankful", "感激的", "I'm so grateful for your help.", "我非常感激你的幫忙。", "Emotions"),
        VocabularyWord("impressed", "adj.", "/ɪmˈprɛst/", "feeling admiration", "印象深刻的", "I was really impressed by your presentation.", "你的簡報讓我印象深刻。", "Emotions"),
        VocabularyWord("nervous", "adj.", "/ˈnɜːrvəs/", "easily worried or anxious", "緊張的", "I always get nervous before public speaking.", "我在公開演講前總是很緊張。", "Emotions"),
        VocabularyWord("patient", "adj.", "/ˈpeɪʃənt/", "able to wait without getting annoyed", "有耐心的", "Please be patient; the doctor will see you soon.", "請耐心等候，醫生很快就會看你。", "Emotions"),

        // ====== Education ======
        VocabularyWord("assignment", "n.", "/əˈsaɪnmənt/", "a task given as part of studies", "作業；任務", "The assignment is due next Monday.", "作業下週一到期。", "Education"),
        VocabularyWord("certificate", "n.", "/sərˈtɪfɪkət/", "an official document proving something", "證書", "I received a certificate for completing the course.", "我完成課程後收到了一張證書。", "Education"),
        VocabularyWord("concentrate", "v.", "/ˈkɒnsəntreɪt/", "to focus one's attention", "專注", "I can't concentrate when there's too much noise.", "太吵的時候我無法專注。", "Education"),
        VocabularyWord("encyclopedia", "n.", "/ɪnˌsaɪkləˈpiːdiə/", "a book containing information on many subjects", "百科全書", "I looked it up in an online encyclopedia.", "我在線上百科全書查了一下。", "Education"),
        VocabularyWord("graduation", "n.", "/ˌɡrædʒuˈeɪʃən/", "the ceremony of receiving a degree", "畢業", "Her graduation ceremony is next Saturday.", "她的畢業典禮是下週六。", "Education"),
        VocabularyWord("knowledge", "n.", "/ˈnɒlɪdʒ/", "information and understanding", "知識", "Reading is a great way to gain knowledge.", "閱讀是獲取知識的好方法。", "Education"),
        VocabularyWord("memorize", "v.", "/ˈmɛməraɪz/", "to learn something by heart", "記住；背誦", "You need to memorize these vocabulary words.", "你需要背誦這些單字。", "Education"),
        VocabularyWord("opportunity", "n.", "/ˌɒpərˈtuːnɪti/", "a favorable circumstance", "機會", "Studying abroad is a wonderful opportunity.", "出國留學是一個很好的機會。", "Education"),
        VocabularyWord("participate", "v.", "/pɑːrˈtɪsɪpeɪt/", "to take part in an activity", "參加；參與", "Everyone is encouraged to participate in the discussion.", "鼓勵每個人參與討論。", "Education"),
        VocabularyWord("semester", "n.", "/sɪˈmɛstər/", "a half-year term in school", "學期", "I'm taking five courses this semester.", "這學期我修了五門課。", "Education"),

        // ====== Technology ======
        VocabularyWord("application", "n.", "/ˌæplɪˈkeɪʃən/", "a computer program for a specific purpose", "應用程式；申請", "Download the application from the app store.", "從應用商店下載這個應用程式。", "Technology"),
        VocabularyWord("battery", "n.", "/ˈbætəri/", "a device that stores electrical energy", "電池", "My phone battery is almost dead.", "我的手機電池快沒電了。", "Technology"),
        VocabularyWord("connection", "n.", "/kəˈnɛkʃən/", "a link between things", "連接；網路連線", "The Wi-Fi connection here is very slow.", "這裡的 Wi-Fi 連線很慢。", "Technology"),
        VocabularyWord("device", "n.", "/dɪˈvaɪs/", "a piece of equipment made for a purpose", "設備；裝置", "This device can translate speech in real time.", "這個裝置可以即時翻譯語音。", "Technology"),
        VocabularyWord("download", "v./n.", "/ˈdaʊnloʊd/", "to copy data from the internet", "下載", "I need to download the latest update.", "我需要下載最新的更新。", "Technology"),
        VocabularyWord("keyboard", "n.", "/ˈkiːbɔːrd/", "a set of keys for typing", "鍵盤", "I spilled coffee on my keyboard.", "我把咖啡灑在鍵盤上了。", "Technology"),
        VocabularyWord("notification", "n.", "/ˌnoʊtɪfɪˈkeɪʃən/", "a message or alert", "通知", "I keep getting notifications from this app.", "我一直收到這個應用程式的通知。", "Technology"),
        VocabularyWord("password", "n.", "/ˈpæswɜːrd/", "a secret word for access", "密碼", "Please create a strong password for your account.", "請為你的帳號建立一個強密碼。", "Technology"),
        VocabularyWord("software", "n.", "/ˈsɒftwɛr/", "programs used by a computer", "軟體", "We need to update the software on this computer.", "我們需要更新這台電腦的軟體。", "Technology"),
        VocabularyWord("website", "n.", "/ˈwɛbsaɪt/", "a location on the internet", "網站", "You can find more information on our website.", "你可以在我們的網站找到更多資訊。", "Technology"),

        // ====== Shopping ======
        VocabularyWord("bargain", "n./v.", "/ˈbɑːrɡɪn/", "a good deal; to negotiate price", "便宜貨；討價還價", "This jacket was a real bargain at half price.", "這件外套半價真的很划算。", "Shopping"),
        VocabularyWord("brand", "n.", "/brænd/", "a product made by a company", "品牌", "What brand of sneakers do you prefer?", "你偏好什麼品牌的運動鞋？", "Shopping"),
        VocabularyWord("cashier", "n.", "/kæˈʃɪr/", "a person who handles payments", "收銀員", "The cashier gave me the wrong change.", "收銀員找錯錢給我了。", "Shopping"),
        VocabularyWord("discount", "n.", "/ˈdɪskaʊnt/", "a reduction in the usual price", "折扣", "Students can get a 10% discount.", "學生可以得到九折優惠。", "Shopping"),
        VocabularyWord("exchange", "v./n.", "/ɪksˈtʃeɪndʒ/", "to give and receive something in return", "交換；換貨", "Can I exchange this for a larger size?", "我可以換大一號的嗎？", "Shopping"),
        VocabularyWord("refund", "n./v.", "/ˈriːfʌnd/", "money returned for a returned item", "退款", "I'd like to request a refund, please.", "我想申請退款，謝謝。", "Shopping"),
        VocabularyWord("warranty", "n.", "/ˈwɒrənti/", "a guarantee for a product", "保固", "This laptop comes with a two-year warranty.", "這台筆電附有兩年保固。", "Shopping"),

        // ====== Nature & Weather ======
        VocabularyWord("atmosphere", "n.", "/ˈætməsfɪr/", "the air surrounding the earth", "大氣層；氣氛", "The atmosphere in the café was very relaxing.", "這家咖啡廳的氣氛很放鬆。", "Nature"),
        VocabularyWord("breeze", "n.", "/briːz/", "a gentle wind", "微風", "A cool breeze was blowing from the ocean.", "一陣涼爽的微風從海上吹來。", "Nature"),
        VocabularyWord("drought", "n.", "/draʊt/", "a long period without rain", "乾旱", "The drought has lasted for several months.", "這場乾旱已經持續了好幾個月。", "Nature"),
        VocabularyWord("forecast", "n./v.", "/ˈfɔːrkæst/", "a prediction of future weather", "天氣預報；預測", "The forecast says it will rain tomorrow.", "天氣預報說明天會下雨。", "Nature"),
        VocabularyWord("humidity", "n.", "/hjuːˈmɪdɪti/", "the amount of moisture in the air", "濕度", "The humidity today is unusually high.", "今天的濕度異常地高。", "Nature"),
        VocabularyWord("lightning", "n.", "/ˈlaɪtnɪŋ/", "a flash of light in the sky during a storm", "閃電", "The lightning lit up the entire sky.", "閃電照亮了整個天空。", "Nature"),
        VocabularyWord("scenery", "n.", "/ˈsiːnəri/", "the natural features of a landscape", "風景", "The mountain scenery here is breathtaking.", "這裡的山景令人驚嘆。", "Nature"),
        VocabularyWord("sunset", "n.", "/ˈsʌnsɛt/", "the time when the sun goes down", "日落", "We watched a beautiful sunset by the lake.", "我們在湖邊欣賞美麗的日落。", "Nature"),
        VocabularyWord("temperature", "n.", "/ˈtɛmprɪtʃər/", "the degree of heat or cold", "溫度", "The temperature dropped suddenly last night.", "昨晚溫度突然下降。", "Nature"),
        VocabularyWord("volcano", "n.", "/vɒlˈkeɪnoʊ/", "a mountain that can erupt", "火山", "The volcano hasn't erupted in over a hundred years.", "這座火山已經一百多年沒有噴發了。", "Nature"),

        // ====== Social & Communication ======
        VocabularyWord("apologize", "v.", "/əˈpɒlədʒaɪz/", "to say sorry", "道歉", "I want to apologize for being late.", "我想為遲到道歉。", "Social"),
        VocabularyWord("celebration", "n.", "/ˌsɛlɪˈbreɪʃən/", "a joyful event or party", "慶祝活動", "The birthday celebration was a lot of fun.", "生日慶祝活動非常有趣。", "Social"),
        VocabularyWord("compliment", "n./v.", "/ˈkɒmplɪmənt/", "a polite expression of praise", "讚美", "She gave me a nice compliment about my presentation.", "她讚美了我的簡報。", "Social"),
        VocabularyWord("conversation", "n.", "/ˌkɒnvərˈseɪʃən/", "an informal talk", "對話", "We had a great conversation over dinner.", "我們在晚餐時有一段很棒的對話。", "Social"),
        VocabularyWord("encourage", "v.", "/ɪnˈkʌrɪdʒ/", "to give support or confidence", "鼓勵", "My parents always encourage me to try new things.", "我的父母總是鼓勵我嘗試新事物。", "Social"),
        VocabularyWord("introduce", "v.", "/ˌɪntrəˈdjuːs/", "to make someone known to another", "介紹", "Let me introduce you to my friend.", "讓我介紹你認識我的朋友。", "Social"),
        VocabularyWord("invitation", "n.", "/ˌɪnvɪˈteɪʃən/", "a request to attend something", "邀請", "Thank you for the invitation to your party.", "謝謝你邀請我參加你的派對。", "Social"),
        VocabularyWord("volunteer", "n./v.", "/ˌvɒlənˈtɪr/", "a person who works without pay", "志工；自願", "She volunteers at the local animal shelter.", "她在當地的動物收容所擔任志工。", "Social"),

        // ====== Transportation ======
        VocabularyWord("commute", "n./v.", "/kəˈmjuːt/", "a regular journey to and from work", "通勤", "My daily commute takes about forty minutes.", "我每天的通勤大約要四十分鐘。", "Transport"),
        VocabularyWord("intersection", "n.", "/ˌɪntərˈsɛkʃən/", "a place where roads cross", "十字路口", "Turn left at the next intersection.", "在下一個路口左轉。", "Transport"),
        VocabularyWord("pedestrian", "n.", "/pəˈdɛstriən/", "a person walking on a street", "行人", "Drivers must stop for pedestrians at the crosswalk.", "駕駛人必須在人行道讓行人先行。", "Transport"),
        VocabularyWord("platform", "n.", "/ˈplætfɔːrm/", "a raised area at a train station", "月台", "The train departs from platform 3.", "火車從第三月台出發。", "Transport"),
        VocabularyWord("route", "n.", "/ruːt/", "a way or path from one place to another", "路線", "What's the fastest route to the airport?", "去機場最快的路線是什麼？", "Transport"),
        VocabularyWord("traffic", "n.", "/ˈtræfɪk/", "vehicles moving on a road", "交通", "There's a lot of traffic during rush hour.", "尖峰時段交通很壅塞。", "Transport"),

        // ====== Home & Living ======
        VocabularyWord("appliance", "n.", "/əˈplaɪəns/", "a device for household use", "家電", "We need to buy a new kitchen appliance.", "我們需要買一個新的廚房家電。", "Home"),
        VocabularyWord("basement", "n.", "/ˈbeɪsmənt/", "an underground floor of a building", "地下室", "We store old furniture in the basement.", "我們把舊家具存放在地下室。", "Home"),
        VocabularyWord("ceiling", "n.", "/ˈsiːlɪŋ/", "the upper surface of a room", "天花板", "There's a crack in the ceiling.", "天花板上有一道裂縫。", "Home"),
        VocabularyWord("furniture", "n.", "/ˈfɜːrnɪtʃər/", "movable items in a room", "家具", "We bought some new furniture for the living room.", "我們為客廳買了一些新家具。", "Home"),
        VocabularyWord("landlord", "n.", "/ˈlændlɔːrd/", "a person who rents out property", "房東", "I need to pay rent to my landlord.", "我需要付房租給房東。", "Home"),
        VocabularyWord("maintenance", "n.", "/ˈmeɪntənəns/", "the process of keeping something in good condition", "維修；保養", "The building needs regular maintenance.", "這棟建築需要定期維修。", "Home"),
        VocabularyWord("renovation", "n.", "/ˌrɛnəˈveɪʃən/", "the act of improving a building", "翻新；裝修", "The kitchen renovation took three months.", "廚房的翻新花了三個月。", "Home"),

        // ====== Entertainment ======
        VocabularyWord("audience", "n.", "/ˈɔːdiəns/", "people watching a performance", "觀眾", "The audience gave a standing ovation.", "觀眾起立鼓掌。", "Entertainment"),
        VocabularyWord("documentary", "n.", "/ˌdɒkjʊˈmɛntəri/", "a film presenting facts", "紀錄片", "I watched a fascinating documentary about space.", "我看了一部關於太空的精彩紀錄片。", "Entertainment"),
        VocabularyWord("exhibition", "n.", "/ˌɛksɪˈbɪʃən/", "a public display of items", "展覽", "There's a new art exhibition at the museum.", "博物館有一個新的藝術展覽。", "Entertainment"),
        VocabularyWord("performance", "n.", "/pərˈfɔːrməns/", "an act of presenting entertainment", "表演", "The performance starts at seven o'clock.", "表演在七點開始。", "Entertainment"),
        VocabularyWord("rehearsal", "n.", "/rɪˈhɜːrsəl/", "a practice session before a performance", "彩排", "We have our final rehearsal tomorrow.", "我們明天有最後一次彩排。", "Entertainment"),
        VocabularyWord("spectacular", "adj.", "/spɛkˈtækjʊlər/", "beautiful and impressive", "壯觀的", "The fireworks display was absolutely spectacular.", "煙火表演真的是太壯觀了。", "Entertainment"),

        // ====== Finance ======
        VocabularyWord("account", "n.", "/əˈkaʊnt/", "an arrangement at a bank", "帳戶", "I'd like to open a savings account.", "我想開一個儲蓄帳戶。", "Finance"),
        VocabularyWord("budget", "n./v.", "/ˈbʌdʒɪt/", "a plan for spending money", "預算", "We need to stick to our monthly budget.", "我們需要遵守每月預算。", "Finance"),
        VocabularyWord("deposit", "n./v.", "/dɪˈpɒzɪt/", "money placed into a bank account", "存款；押金", "I made a deposit of five hundred dollars.", "我存了五百美元。", "Finance"),
        VocabularyWord("expense", "n.", "/ɪkˈspɛns/", "the cost of something", "費用；開銷", "Travel expenses can add up quickly.", "旅行費用可能很快就會累積起來。", "Finance"),
        VocabularyWord("investment", "n.", "/ɪnˈvɛstmənt/", "money put into something for profit", "投資", "Real estate can be a good investment.", "不動產可以是一個好的投資。", "Finance"),
        VocabularyWord("mortgage", "n.", "/ˈmɔːrɡɪdʒ/", "a loan for buying property", "房貸", "They're paying off their mortgage.", "他們正在償還房貸。", "Finance"),

        // ====== Relationships ======
        VocabularyWord("acquaintance", "n.", "/əˈkweɪntəns/", "a person you know slightly", "認識的人", "He's more of an acquaintance than a close friend.", "他比較像是認識的人而不是親密的朋友。", "Relationships"),
        VocabularyWord("companion", "n.", "/kəmˈpæniən/", "a person who accompanies another", "同伴", "A dog is a wonderful companion.", "狗是很好的同伴。", "Relationships"),
        VocabularyWord("considerate", "adj.", "/kənˈsɪdərɪt/", "careful not to cause inconvenience", "體貼的", "She is always very considerate of others.", "她總是很體貼他人。", "Relationships"),
        VocabularyWord("dependable", "adj.", "/dɪˈpɛndəbəl/", "able to be relied on", "可靠的", "He is the most dependable person I know.", "他是我認識的最可靠的人。", "Relationships"),
        VocabularyWord("sincere", "adj.", "/sɪnˈsɪr/", "genuine and honest", "真誠的", "I gave him my sincere thanks.", "我真誠地感謝他。", "Relationships"),

        // ====== Environment ======
        VocabularyWord("conservation", "n.", "/ˌkɒnsərˈveɪʃən/", "the protection of natural resources", "保育；保護", "Wildlife conservation is very important.", "野生動物保育非常重要。", "Environment"),
        VocabularyWord("pollution", "n.", "/pəˈluːʃən/", "the presence of harmful substances", "污染", "Air pollution is a serious problem in many cities.", "空氣污染在許多城市是嚴重的問題。", "Environment"),
        VocabularyWord("recycle", "v.", "/riːˈsaɪkəl/", "to convert waste into reusable material", "回收", "Remember to recycle your plastic bottles.", "記得回收你的塑膠瓶。", "Environment"),
        VocabularyWord("sustainable", "adj.", "/səˈsteɪnəbəl/", "able to be maintained long-term", "永續的", "We should support sustainable farming.", "我們應該支持永續農業。", "Environment"),

        // ====== Miscellaneous Useful Words ======
        VocabularyWord("absolutely", "adv.", "/ˌæbsəˈluːtli/", "completely; without doubt", "絕對地；完全地", "You are absolutely right about that.", "關於那件事你完全正確。", "Common"),
        VocabularyWord("actually", "adv.", "/ˈæktʃuəli/", "in fact; really", "其實；實際上", "Actually, I changed my mind.", "其實，我改主意了。", "Common"),
        VocabularyWord("approximately", "adv.", "/əˈprɒksɪmɪtli/", "close to an exact amount", "大約", "It takes approximately two hours to get there.", "到那裡大約需要兩小時。", "Common"),
        VocabularyWord("definitely", "adv.", "/ˈdɛfɪnɪtli/", "without doubt; certainly", "肯定地", "I will definitely come to your party.", "我一定會去你的派對。", "Common"),
        VocabularyWord("essential", "adj.", "/ɪˈsɛnʃəl/", "absolutely necessary", "必要的；不可或缺的", "Water is essential for life.", "水是生命不可或缺的。", "Common"),
        VocabularyWord("fortunately", "adv.", "/ˈfɔːrtʃənɪtli/", "by good luck", "幸運地", "Fortunately, nobody was hurt in the accident.", "幸運的是，意外中沒有人受傷。", "Common"),
        VocabularyWord("immediately", "adv.", "/ɪˈmiːdiətli/", "at once; without delay", "立刻", "Please call me back immediately.", "請立刻回電給我。", "Common"),
        VocabularyWord("obviously", "adv.", "/ˈɒbviəsli/", "in a way that is easy to see", "顯然地", "She was obviously very happy about the news.", "她顯然對這個消息非常開心。", "Common"),
        VocabularyWord("probably", "adv.", "/ˈprɒbəbli/", "almost certainly", "可能；大概", "It will probably rain this afternoon.", "今天下午可能會下雨。", "Common"),
        VocabularyWord("thoroughly", "adv.", "/ˈθʌrəli/", "in a complete way", "徹底地", "Please clean the kitchen thoroughly.", "請徹底清潔廚房。", "Common")
    )
}

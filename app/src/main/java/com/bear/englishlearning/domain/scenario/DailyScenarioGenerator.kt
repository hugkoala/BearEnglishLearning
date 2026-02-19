package com.bear.englishlearning.domain.scenario

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Generates a daily scenario with 10 example sentences.
 * Uses date-based seed so the same scenario appears all day.
 * 20 scenario templates, each with 10 sentences.
 */
@Singleton
class DailyScenarioGenerator @Inject constructor() {

    fun generateForToday(): GeneratedScenario {
        return generateForDate(LocalDate.now())
    }

    fun generateForDate(date: LocalDate): GeneratedScenario {
        val seed = date.toEpochDay()
        val random = Random(seed)
        val all = getAllScenarios()
        return all[random.nextInt(all.size)]
    }

    private fun getAllScenarios(): List<GeneratedScenario> = listOf(
        // 1
        GeneratedScenario("At the Bakery", "在麵包店", "daily", listOf(
            GeneratedSentence("I'd like two croissants, please.", "我想要兩個可頌，謝謝。", "'croissant' 發音 /krwɑːˈsɒ̃/ 法語借詞"),
            GeneratedSentence("Do you have any whole wheat bread?", "你們有全麥麵包嗎？", "'whole wheat' 連讀為 /hoʊl wiːt/"),
            GeneratedSentence("How much is the chocolate cake?", "巧克力蛋糕多少錢？", "'chocolate' 重音在第一音節 /ˈtʃɒklɪt/"),
            GeneratedSentence("Is this baked fresh today?", "這是今天新鮮烤的嗎？", "'baked' 過去式 /beɪkt/ 注意尾音"),
            GeneratedSentence("Can I get a dozen donuts?", "我可以買一打甜甜圈嗎？", "'dozen' 重音在第一音節 /ˈdʌzən/"),
            GeneratedSentence("What's your most popular item?", "你們最受歡迎的品項是什麼？", "'popular' 重音在第一音節 /ˈpɒpjʊlər/"),
            GeneratedSentence("I'm looking for a birthday cake.", "我在找生日蛋糕。", "'birthday' 是複合名詞，重音在 'birth'"),
            GeneratedSentence("Does this contain any nuts?", "這裡面有堅果嗎？", "'contain' 重音在第二音節 /kənˈteɪn/"),
            GeneratedSentence("Can you write a message on the cake?", "你可以在蛋糕上寫字嗎？", "'message' 重音在第一音節 /ˈmɛsɪdʒ/"),
            GeneratedSentence("I'll take one of each, please.", "每種各來一個，謝謝。", "'one of each' 是常用點餐表達")
        )),
        // 2
        GeneratedScenario("At the Pet Store", "在寵物店", "daily", listOf(
            GeneratedSentence("I'm looking for food for my cat.", "我在找貓糧。", "'looking for' 連讀為 /ˈlʊkɪŋ fɔːr/"),
            GeneratedSentence("How often should I feed my puppy?", "我應該多久餵一次小狗？", "'puppy' 注意 /ˈpʌpi/ 短母音"),
            GeneratedSentence("Do you sell fish tanks?", "你們賣魚缸嗎？", "'fish tank' 是複合名詞"),
            GeneratedSentence("My dog needs a new leash.", "我的狗需要一條新牽繩。", "'leash' 發音 /liːʃ/"),
            GeneratedSentence("Which brand of pet food do you recommend?", "你推薦什麼品牌的寵物糧？", "'brand' 注意 /brænd/ 的短母音"),
            GeneratedSentence("Can I bring my dog inside the store?", "我可以帶我的狗進店裡嗎？", "'bring' 和 'inside' 注意語調"),
            GeneratedSentence("I need some flea medicine for my cat.", "我需要一些貓的除蚤藥。", "'flea' 發音 /fliː/"),
            GeneratedSentence("How big will this breed get?", "這個品種會長多大？", "'breed' 發音 /briːd/"),
            GeneratedSentence("Do you offer grooming services?", "你們提供美容服務嗎？", "'grooming' 重音在第一音節 /ˈɡruːmɪŋ/"),
            GeneratedSentence("She's a rescue dog from the shelter.", "她是從收容所領養的狗。", "'rescue' 重音在第一音節 /ˈrɛskjuː/")
        )),
        // 3
        GeneratedScenario("At the Laundromat", "在自助洗衣店", "daily", listOf(
            GeneratedSentence("Excuse me, how do I use this machine?", "不好意思，這台機器怎麼用？", "'machine' 重音在第二音節 /məˈʃiːn/"),
            GeneratedSentence("Do you have change for a ten?", "你有零錢可以換十元嗎？", "'change' 注意 /tʃeɪndʒ/ 發音"),
            GeneratedSentence("How long does a wash cycle take?", "一個洗衣循環要多久？", "'cycle' 發音 /ˈsaɪkəl/"),
            GeneratedSentence("Is this dryer available?", "這台烘衣機可以用嗎？", "'dryer' 發音 /ˈdraɪər/"),
            GeneratedSentence("I need some detergent.", "我需要一些洗衣精。", "'detergent' 重音在第二音節 /dɪˈtɜːrdʒənt/"),
            GeneratedSentence("Can I leave my clothes here while they dry?", "衣服烘乾的時候我可以先離開嗎？", "'while' 連接詞表示同時"),
            GeneratedSentence("This machine ate my quarters!", "這台機器吞了我的硬幣！", "'quarters' 指 25 分美金硬幣"),
            GeneratedSentence("Where can I fold my laundry?", "我可以在哪裡摺衣服？", "'fold' 注意 /foʊld/ 雙母音"),
            GeneratedSentence("Do you have a lost and found?", "你們有失物招領處嗎？", "'lost and found' 是固定搭配"),
            GeneratedSentence("What temperature should I use for colors?", "彩色衣物應該用什麼溫度？", "'temperature' 重音在第一音節")
        )),
        // 4
        GeneratedScenario("At the Car Repair Shop", "在汽車修理廠", "daily", listOf(
            GeneratedSentence("My car is making a strange noise.", "我的車發出奇怪的聲音。", "'strange' 注意 /streɪndʒ/ 子音群"),
            GeneratedSentence("How long will the repair take?", "修理需要多久？", "'repair' 重音在第二音節 /rɪˈpɛr/"),
            GeneratedSentence("Can you give me an estimate?", "你可以給我一個估價嗎？", "'estimate' 名詞重音在第一音節 /ˈɛstɪmɪt/"),
            GeneratedSentence("I need an oil change.", "我需要換機油。", "'oil change' 是固定搭配"),
            GeneratedSentence("The check engine light is on.", "引擎故障燈亮了。", "'check engine light' 汽車專用術語"),
            GeneratedSentence("Is it covered by warranty?", "保固有涵蓋嗎？", "'warranty' 重音在第一音節 /ˈwɒrənti/"),
            GeneratedSentence("My brakes feel a little soft.", "我的煞車感覺有點軟。", "'brakes' 注意 /breɪks/ 發音"),
            GeneratedSentence("Do you have any loaner cars?", "你們有代步車嗎？", "'loaner' 發音 /ˈloʊnər/"),
            GeneratedSentence("When can I pick up my car?", "我什麼時候可以來取車？", "'pick up' 片語動詞表示取回"),
            GeneratedSentence("The air conditioning isn't working.", "冷氣壞了。", "'air conditioning' 常縮寫為 A/C")
        )),
        // 5
        GeneratedScenario("At the Dentist", "在牙醫", "daily", listOf(
            GeneratedSentence("I have a toothache.", "我牙痛。", "'toothache' 是複合名詞 /ˈtuːθeɪk/"),
            GeneratedSentence("When was your last dental checkup?", "你上次牙科檢查是什麼時候？", "'checkup' 是複合名詞，重音在 'check'"),
            GeneratedSentence("I need to schedule a cleaning.", "我需要預約洗牙。", "'schedule' 美式 /ˈskɛdʒuːl/"),
            GeneratedSentence("Does this procedure hurt?", "這個療程會痛嗎？", "'procedure' 重音在第二音節 /prəˈsiːdʒər/"),
            GeneratedSentence("I'm allergic to certain anesthetics.", "我對某些麻醉劑過敏。", "'anesthetic' 重音在第三音節"),
            GeneratedSentence("How many cavities do I have?", "我有幾個蛀牙？", "'cavities' 發音 /ˈkævɪtiz/"),
            GeneratedSentence("Will I need a crown?", "我需要做牙冠嗎？", "'crown' 牙醫用語指牙冠 /kraʊn/"),
            GeneratedSentence("My gums have been bleeding.", "我的牙齦一直在流血。", "'gums' 指牙齦 /ɡʌmz/"),
            GeneratedSentence("Can you recommend a good toothbrush?", "你能推薦一支好的牙刷嗎？", "'toothbrush' 複合名詞 /ˈtuːθbrʌʃ/"),
            GeneratedSentence("I'd like to whiten my teeth.", "我想美白牙齒。", "'whiten' 動詞 /ˈwaɪtən/")
        )),
        // 6
        GeneratedScenario("At the Farmer's Market", "在農夫市集", "daily", listOf(
            GeneratedSentence("Are these strawberries organic?", "這些草莓是有機的嗎？", "'organic' 重音在第二音節 /ɔːrˈɡænɪk/"),
            GeneratedSentence("How much for a pound of tomatoes?", "一磅番茄多少錢？", "'pound' 是重量單位約 0.45 公斤"),
            GeneratedSentence("These peaches look really fresh.", "這些桃子看起來很新鮮。", "'peaches' 注意 /ˈpiːtʃɪz/ 複數"),
            GeneratedSentence("Do you grow these yourself?", "這些是你自己種的嗎？", "'grow' 過去式為 'grew' /ɡruː/"),
            GeneratedSentence("Can I try a sample?", "我可以試吃嗎？", "'sample' 重音在第一音節 /ˈsæmpəl/"),
            GeneratedSentence("I'll take a basket of blueberries.", "我要一籃藍莓。", "'basket' 重音在第一音節 /ˈbæskɪt/"),
            GeneratedSentence("When is the market open?", "市集什麼時候營業？", "'market' 重音在第一音節 /ˈmɑːrkɪt/"),
            GeneratedSentence("Do you accept credit cards?", "你們收信用卡嗎？", "'accept' 重音在第二音節 /əkˈsɛpt/"),
            GeneratedSentence("This honey is locally produced.", "這蜂蜜是在地生產的。", "'locally' 副詞 /ˈloʊkəli/"),
            GeneratedSentence("I'd like some fresh herbs.", "我想要一些新鮮香草。", "'herbs' 美式發音 /ɜːrbz/ h 不發音")
        )),
        // 7
        GeneratedScenario("At the Bookstore", "在書店", "daily", listOf(
            GeneratedSentence("I'm looking for the latest bestseller.", "我在找最新的暢銷書。", "'bestseller' 複合名詞 /ˌbɛstˈsɛlər/"),
            GeneratedSentence("Do you have this book in paperback?", "這本書有平裝版嗎？", "'paperback' 指平裝書 /ˈpeɪpərbæk/"),
            GeneratedSentence("Can you order this book for me?", "你可以幫我訂這本書嗎？", "'order' 在此指訂購 /ˈɔːrdər/"),
            GeneratedSentence("Where is the children's section?", "兒童區在哪裡？", "'section' 重音在第一音節 /ˈsɛkʃən/"),
            GeneratedSentence("I'm interested in science fiction.", "我對科幻小說有興趣。", "'science fiction' 常縮寫為 sci-fi"),
            GeneratedSentence("Do you have a reading recommendation?", "你有推薦的書嗎？", "'recommendation' 重音在第四音節"),
            GeneratedSentence("Is there a café inside the bookstore?", "書店裡有咖啡廳嗎？", "'café' 法語借詞 /kæˈfeɪ/"),
            GeneratedSentence("I'd like to browse for a while.", "我想先看看。", "'browse' 表示隨意瀏覽 /braʊz/"),
            GeneratedSentence("Can I return this if I don't like it?", "如果我不喜歡可以退嗎？", "'return' 重音在第二音節 /rɪˈtɜːrn/"),
            GeneratedSentence("Do you have a loyalty program?", "你們有會員集點嗎？", "'loyalty' 重音在第一音節 /ˈlɔɪəlti/")
        )),
        // 8
        GeneratedScenario("Moving to a New Apartment", "搬新家", "daily", listOf(
            GeneratedSentence("When can I move in?", "我什麼時候可以搬進去？", "'move in' 片語動詞表示遷入"),
            GeneratedSentence("Is the deposit refundable?", "押金可以退還嗎？", "'refundable' 重音在第二音節 /rɪˈfʌndəbəl/"),
            GeneratedSentence("Does the rent include utilities?", "房租包含水電嗎？", "'utilities' 指水電瓦斯 /juːˈtɪlɪtiz/"),
            GeneratedSentence("I need to set up the internet.", "我需要設定網路。", "'set up' 片語動詞表示安裝"),
            GeneratedSentence("Where should I put the sofa?", "沙發應該放哪裡？", "'sofa' 發音 /ˈsoʊfə/"),
            GeneratedSentence("Can you help me carry these boxes?", "你可以幫我搬這些箱子嗎？", "'carry' 注意 /ˈkæri/ 短母音"),
            GeneratedSentence("The elevator is too small for this table.", "電梯太小放不下這張桌子。", "'elevator' 美式用法，英式用 'lift'"),
            GeneratedSentence("I need to change my address.", "我需要更改我的地址。", "'address' 名詞重音在第一音節"),
            GeneratedSentence("Do we need to sign a lease?", "我們需要簽租約嗎？", "'lease' 發音 /liːs/ 指租約"),
            GeneratedSentence("The neighbors seem really friendly.", "鄰居看起來很友善。", "'neighbors' 注意 /ˈneɪbərz/ 發音")
        )),
        // 9
        GeneratedScenario("At the Dry Cleaner", "在乾洗店", "daily", listOf(
            GeneratedSentence("I need this suit dry cleaned.", "我需要乾洗這套西裝。", "'dry cleaned' 是形容詞用法"),
            GeneratedSentence("Can you remove this stain?", "你可以去掉這個汙漬嗎？", "'stain' 發音 /steɪn/"),
            GeneratedSentence("When will it be ready?", "什麼時候會好？", "'ready' 重音在第一音節 /ˈrɛdi/"),
            GeneratedSentence("I need this by tomorrow morning.", "我明天早上之前需要。", "'by' 在此表示截止時間"),
            GeneratedSentence("Can you press these pants?", "你可以燙這條褲子嗎？", "'press' 在此指熨燙 /prɛs/"),
            GeneratedSentence("Do you do alterations?", "你們做修改嗎？", "'alterations' 指衣服修改 /ˌɔːltəˈreɪʃənz/"),
            GeneratedSentence("This button is loose.", "這顆鈕扣鬆了。", "'loose' 形容詞 /luːs/ 不要跟 'lose' 搞混"),
            GeneratedSentence("How much for a dress shirt?", "洗一件襯衫多少錢？", "'dress shirt' 指正式襯衫"),
            GeneratedSentence("I'd like starch on the collar.", "我想在領子上漿。", "'starch' 發音 /stɑːrtʃ/ 指漿衣"),
            GeneratedSentence("Here's my receipt from last time.", "這是我上次的收據。", "'receipt' 注意 p 不發音 /rɪˈsiːt/")
        )),
        // 10
        GeneratedScenario("At the Eye Doctor", "在眼科", "daily", listOf(
            GeneratedSentence("I need to get my eyes checked.", "我需要檢查眼睛。", "'get checked' 常用被動表達"),
            GeneratedSentence("My vision has been getting blurry.", "我的視力變得模糊。", "'blurry' 形容詞 /ˈblɜːri/"),
            GeneratedSentence("I'd like to try contact lenses.", "我想試戴隱形眼鏡。", "'contact lenses' 即隱形眼鏡"),
            GeneratedSentence("Can you read the bottom line?", "你可以讀最下面那行嗎？", "'bottom' 重音在第一音節 /ˈbɒtəm/"),
            GeneratedSentence("Do I need a stronger prescription?", "我需要更深的度數嗎？", "'prescription' 在此指眼鏡處方"),
            GeneratedSentence("How often should I replace my contacts?", "我應該多久換一次隱形眼鏡？", "'replace' 重音在第二音節 /rɪˈpleɪs/"),
            GeneratedSentence("These frames look great on you.", "這副鏡框很適合你。", "'frames' 指眼鏡框 /freɪmz/"),
            GeneratedSentence("I've been getting frequent headaches.", "我最近常頭痛。", "'frequent' 重音在第一音節 /ˈfriːkwənt/"),
            GeneratedSentence("Are there any blue-light blocking glasses?", "有防藍光眼鏡嗎？", "'blue-light blocking' 現代用語"),
            GeneratedSentence("My insurance covers one pair a year.", "我的保險一年涵蓋一副。", "'insurance' 重音在第二音節 /ɪnˈʃʊrəns/")
        )),
        // 11
        GeneratedScenario("At the Playground", "在遊樂場", "daily", listOf(
            GeneratedSentence("Be careful on the slide!", "溜滑梯要小心！", "'slide' 發音 /slaɪd/"),
            GeneratedSentence("Do you want to go on the swings?", "你想去盪鞦韆嗎？", "'swings' 注意 /swɪŋz/ 鼻音"),
            GeneratedSentence("Let's take turns on the seesaw.", "我們輪流玩蹺蹺板吧。", "'take turns' 表示輪流 /teɪk tɜːrnz/"),
            GeneratedSentence("Hold on tight!", "抓緊！", "'hold on' 片語表示抓住"),
            GeneratedSentence("Can you push me higher?", "你可以推我高一點嗎？", "'higher' 比較級 /ˈhaɪər/"),
            GeneratedSentence("It's your turn now.", "現在輪到你了。", "'your turn' 是常用表達"),
            GeneratedSentence("Don't run near the sandbox.", "不要在沙坑附近跑。", "'sandbox' 複合名詞 /ˈsændbɒks/"),
            GeneratedSentence("Let's play hide and seek!", "我們來玩捉迷藏！", "'hide and seek' 固定搭配"),
            GeneratedSentence("Are you having fun?", "你玩得開心嗎？", "'having fun' 現在進行式"),
            GeneratedSentence("Time to go home, sweetie.", "該回家了，寶貝。", "'sweetie' 愛稱 /ˈswiːti/")
        )),
        // 12
        GeneratedScenario("At the Electronics Store", "在電子用品店", "daily", listOf(
            GeneratedSentence("I'm looking for a new laptop.", "我在找一台新的筆電。", "'laptop' 複合名詞 /ˈlæptɒp/"),
            GeneratedSentence("What's the battery life on this one?", "這台的電池續航力多長？", "'battery life' 電子產品常用詞"),
            GeneratedSentence("Does it come with a warranty?", "有附保固嗎？", "'warranty' 重音在第一音節 /ˈwɒrənti/"),
            GeneratedSentence("How much storage does it have?", "它有多少儲存空間？", "'storage' 重音在第一音節 /ˈstɔːrɪdʒ/"),
            GeneratedSentence("Can I see it in a different color?", "我可以看其他顏色嗎？", "'different' 重音在第一音節 /ˈdɪfərənt/"),
            GeneratedSentence("Is this compatible with my phone?", "這跟我的手機相容嗎？", "'compatible' 重音在第二音節 /kəmˈpætɪbəl/"),
            GeneratedSentence("I'd like to trade in my old phone.", "我想用舊手機折抵。", "'trade in' 片語動詞表示以舊換新"),
            GeneratedSentence("Do you offer a student discount?", "你們有學生折扣嗎？", "'discount' 重音在第一音節 /ˈdɪskaʊnt/"),
            GeneratedSentence("Can you help me set this up?", "你可以幫我設定嗎？", "'set up' 片語動詞表示安裝設定"),
            GeneratedSentence("I'll also need a screen protector.", "我還需要一個螢幕保護貼。", "'screen protector' 手機配件用語")
        )),
        // 13
        GeneratedScenario("Ordering Food Delivery", "叫外送", "daily", listOf(
            GeneratedSentence("I'd like to place an order for delivery.", "我想叫外送。", "'place an order' 是正式的點餐用法"),
            GeneratedSentence("How long will the delivery take?", "外送大概要多久？", "'delivery' 重音在第二音節 /dɪˈlɪvəri/"),
            GeneratedSentence("Is there a minimum order amount?", "有最低消費嗎？", "'minimum' 重音在第一音節 /ˈmɪnɪməm/"),
            GeneratedSentence("Can I add extra sauce on the side?", "我可以多要一份醬料嗎？", "'on the side' 表示另外放"),
            GeneratedSentence("What's the delivery fee?", "外送費是多少？", "'fee' 發音 /fiː/ 長母音"),
            GeneratedSentence("I'll pay with my credit card.", "我用信用卡付。", "'pay with' 表示用...付款"),
            GeneratedSentence("Please don't ring the doorbell.", "請不要按門鈴。", "'doorbell' 複合名詞 /ˈdɔːrbɛl/"),
            GeneratedSentence("The driver can't find my address.", "司機找不到我的地址。", "'can't find' 注意 can't 美式 /kænt/"),
            GeneratedSentence("My order is missing an item.", "我的訂單少了一樣。", "'missing' 表示遺漏的 /ˈmɪsɪŋ/"),
            GeneratedSentence("Can I leave a tip through the app?", "我可以在 App 上給小費嗎？", "'tip' 指小費 /tɪp/")
        )),
        // 14
        GeneratedScenario("At the Swimming Pool", "在游泳池", "daily", listOf(
            GeneratedSentence("Is the pool heated?", "游泳池有加熱嗎？", "'heated' 過去分詞用作形容詞 /ˈhiːtɪd/"),
            GeneratedSentence("Do I need to wear a swim cap?", "我需要戴泳帽嗎？", "'swim cap' 是泳池用語"),
            GeneratedSentence("What are the pool hours?", "游泳池的開放時間是？", "'pool hours' 指營業時間"),
            GeneratedSentence("No diving in the shallow end.", "淺水區不能跳水。", "'shallow' 反義詞是 'deep'"),
            GeneratedSentence("Can I rent a towel?", "我可以租一條毛巾嗎？", "'rent' 在此指短期租借 /rɛnt/"),
            GeneratedSentence("The water is really refreshing today.", "今天的水很清爽。", "'refreshing' 重音在第二音節 /rɪˈfrɛʃɪŋ/"),
            GeneratedSentence("Which lane is for lap swimming?", "哪個水道是給游來回的？", "'lap swimming' 指來回游泳"),
            GeneratedSentence("I'm just going to sit by the pool.", "我只是要坐在泳池邊。", "'sit by' 表示坐在旁邊"),
            GeneratedSentence("Please shower before entering the pool.", "進泳池前請先沖澡。", "'shower' 在此用作動詞 /ˈʃaʊər/"),
            GeneratedSentence("My kids are taking swimming lessons.", "我的小孩在上游泳課。", "'lessons' 重音在第一音節 /ˈlɛsənz/")
        )),
        // 15
        GeneratedScenario("At the Furniture Store", "在家具店", "daily", listOf(
            GeneratedSentence("I'm looking for a dining table.", "我在找一張餐桌。", "'dining table' 複合名詞"),
            GeneratedSentence("Can I try sitting on this couch?", "我可以試坐這張沙發嗎？", "'couch' 同義詞是 'sofa' /kaʊtʃ/"),
            GeneratedSentence("Does this come in a different color?", "這有其他顏色嗎？", "'come in' 表示有...款式"),
            GeneratedSentence("How long is the delivery time?", "送貨需要多久？", "'delivery time' 指送貨時間"),
            GeneratedSentence("Do you offer assembly service?", "你們提供組裝服務嗎？", "'assembly' 重音在第二音節 /əˈsɛmbli/"),
            GeneratedSentence("This bookshelf is perfect for my room.", "這個書架很適合我的房間。", "'bookshelf' 複合名詞 /ˈbʊkʃɛlf/"),
            GeneratedSentence("What material is this made of?", "這是什麼材質做的？", "'material' 重音在第二音節 /məˈtɪriəl/"),
            GeneratedSentence("Is there a showroom I can visit?", "有展示間可以參觀嗎？", "'showroom' 複合名詞 /ˈʃoʊruːm/"),
            GeneratedSentence("Can I return it if it doesn't fit?", "如果不合適可以退嗎？", "'fit' 在此指尺寸合適 /fɪt/"),
            GeneratedSentence("I'd like to finance this purchase.", "我想分期付款。", "'finance' 動詞重音在第二音節 /faɪˈnæns/")
        )),
        // 16
        GeneratedScenario("At the Music Store", "在樂器行", "daily", listOf(
            GeneratedSentence("I'd like to try this guitar.", "我想試這把吉他。", "'guitar' 重音在第二音節 /ɡɪˈtɑːr/"),
            GeneratedSentence("Do you give piano lessons?", "你們有鋼琴課嗎？", "'piano' 重音在第二音節 /piˈænoʊ/"),
            GeneratedSentence("I'm a beginner. What do you recommend?", "我是初學者，你推薦什麼？", "'beginner' 重音在第二音節 /bɪˈɡɪnər/"),
            GeneratedSentence("How much is this set of drums?", "這套鼓多少錢？", "'set of drums' 指一套爵士鼓"),
            GeneratedSentence("Can I hear how this sounds?", "我可以聽聽它的聲音嗎？", "'sounds' 在此指聲音效果"),
            GeneratedSentence("I need new strings for my violin.", "我需要新的小提琴弦。", "'strings' 指琴弦 /strɪŋz/"),
            GeneratedSentence("Do you sell sheet music?", "你們賣樂譜嗎？", "'sheet music' 指印刷樂譜"),
            GeneratedSentence("Is this instrument good for kids?", "這個樂器適合小孩嗎？", "'instrument' 重音在第一音節 /ˈɪnstrəmənt/"),
            GeneratedSentence("I'd like to rent a keyboard for a month.", "我想租一台鍵盤一個月。", "'rent' 在此指長期租借"),
            GeneratedSentence("Can you tune my guitar?", "你可以幫我調音嗎？", "'tune' 動詞表示調音 /tjuːn/")
        )),
        // 17
        GeneratedScenario("At Immigration Control", "在出入境管制", "travel", listOf(
            GeneratedSentence("Here is my passport and boarding pass.", "這是我的護照和登機證。", "'boarding pass' 登機證 /ˈbɔːrdɪŋ pæs/"),
            GeneratedSentence("I'm here for vacation.", "我來度假的。", "'vacation' 美式用法，英式用 'holiday'"),
            GeneratedSentence("I'll be staying for two weeks.", "我會待兩週。", "'staying for' 表示停留時間"),
            GeneratedSentence("I'm staying at the Hilton Hotel.", "我住在希爾頓飯店。", "'staying at' 表示住在某處"),
            GeneratedSentence("What's the purpose of your visit?", "你此行的目的是什麼？", "'purpose' 重音在第一音節 /ˈpɜːrpəs/"),
            GeneratedSentence("I have nothing to declare.", "我沒有東西要申報。", "'declare' 海關用語 /dɪˈklɛr/"),
            GeneratedSentence("Where do I pick up my luggage?", "我在哪裡領行李？", "'pick up' 在此指領取"),
            GeneratedSentence("Is there a currency exchange nearby?", "附近有貨幣兌換處嗎？", "'currency exchange' 換匯處"),
            GeneratedSentence("How do I get to the city center?", "我怎麼到市中心？", "'city center' 美式也用 'downtown'"),
            GeneratedSentence("Can I get a transit visa?", "我可以辦過境簽證嗎？", "'transit visa' 過境簽證 /ˈtrænzɪt/")
        )),
        // 18
        GeneratedScenario("Visiting the Museum", "參觀博物館", "daily", listOf(
            GeneratedSentence("How much is the admission fee?", "入場費多少錢？", "'admission' 重音在第二音節 /ədˈmɪʃən/"),
            GeneratedSentence("Is photography allowed inside?", "裡面可以拍照嗎？", "'photography' 重音在第二音節 /fəˈtɒɡrəfi/"),
            GeneratedSentence("Where is the ancient Egypt exhibition?", "古埃及展覽在哪裡？", "'ancient' 重音在第一音節 /ˈeɪnʃənt/"),
            GeneratedSentence("Can I get an audio guide?", "我可以拿語音導覽嗎？", "'audio guide' 語音導覽裝置"),
            GeneratedSentence("This painting is from the 18th century.", "這幅畫是十八世紀的。", "'century' 重音在第一音節 /ˈsɛntʃəri/"),
            GeneratedSentence("The museum closes at six o'clock.", "博物館六點關門。", "'closes at' 表示關閉時間"),
            GeneratedSentence("Is there a gift shop?", "有紀念品店嗎？", "'gift shop' 紀念品商店"),
            GeneratedSentence("I'd like a guided tour, please.", "我想要導覽，謝謝。", "'guided tour' 有人帶領的參觀"),
            GeneratedSentence("Please don't touch the exhibits.", "請不要觸摸展品。", "'exhibits' 名詞指展品 /ɪɡˈzɪbɪts/"),
            GeneratedSentence("This sculpture is absolutely stunning.", "這座雕塑真的太驚豔了。", "'sculpture' 發音 /ˈskʌlptʃər/")
        )),
        // 19
        GeneratedScenario("At the Amusement Park", "在遊樂園", "daily", listOf(
            GeneratedSentence("How long is the wait for this ride?", "這個設施要排多久？", "'wait' 在此指等待時間 /weɪt/"),
            GeneratedSentence("Is there a height requirement?", "有身高限制嗎？", "'height requirement' 身高要求"),
            GeneratedSentence("I want to ride the roller coaster!", "我想坐雲霄飛車！", "'roller coaster' 雲霄飛車"),
            GeneratedSentence("Can we get a fast pass?", "我們可以買快速通關嗎？", "'fast pass' 免排隊票券"),
            GeneratedSentence("Where's the nearest restroom?", "最近的洗手間在哪裡？", "'restroom' 美式用法 /ˈrɛstruːm/"),
            GeneratedSentence("Let's get some cotton candy!", "我們去買棉花糖！", "'cotton candy' 美式，英式用 'candyfloss'"),
            GeneratedSentence("That was so much fun!", "那真的太好玩了！", "'so much fun' 強調程度的表達"),
            GeneratedSentence("I'm too scared to go on that one.", "那個我太害怕了不敢坐。", "'scared' 形容詞 /skɛrd/"),
            GeneratedSentence("Should we watch the fireworks show?", "我們要看煙火秀嗎？", "'fireworks' 複合名詞 /ˈfaɪərwɜːrks/"),
            GeneratedSentence("The park closes at ten tonight.", "遊樂園今晚十點關。", "'closes at' 表示打烊時間")
        )),
        // 20
        GeneratedScenario("At the Yoga Class", "在瑜伽課", "daily", listOf(
            GeneratedSentence("Is this class suitable for beginners?", "這堂課適合初學者嗎？", "'suitable' 重音在第一音節 /ˈsuːtəbəl/"),
            GeneratedSentence("Do I need to bring my own mat?", "我需要自己帶瑜伽墊嗎？", "'mat' 指運動墊 /mæt/"),
            GeneratedSentence("Take a deep breath and relax.", "深呼吸然後放鬆。", "'deep breath' 深呼吸 /diːp brɛθ/"),
            GeneratedSentence("I can't touch my toes yet.", "我還碰不到我的腳趾。", "'touch my toes' 是柔軟度測試"),
            GeneratedSentence("Hold this pose for thirty seconds.", "這個姿勢維持三十秒。", "'pose' 指瑜伽姿勢 /poʊz/"),
            GeneratedSentence("My muscles are really sore today.", "我今天肌肉很痠痛。", "'sore' 形容詞表示痠痛 /sɔːr/"),
            GeneratedSentence("What time is the morning class?", "早上的課是幾點？", "'morning class' 早上班"),
            GeneratedSentence("I feel so relaxed after that session.", "那堂課後我覺得好放鬆。", "'session' 重音在第一音節 /ˈsɛʃən/"),
            GeneratedSentence("Can you show me how to do this stretch?", "你可以示範這個伸展動作嗎？", "'stretch' 名詞指伸展動作 /strɛtʃ/"),
            GeneratedSentence("Namaste. See you next week!", "合十禮。下週見！", "'namaste' 瑜伽用語 /ˌnɑːməˈsteɪ/")
        ))
    )
}

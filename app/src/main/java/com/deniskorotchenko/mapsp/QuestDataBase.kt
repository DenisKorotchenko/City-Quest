package com.deniskorotchenko.mapsp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.android.gms.maps.model.LatLng

class QuestDataBase(context: Context) : SQLiteOpenHelper(context, Singleton.instance.DATABASE_NAME, null, Singleton.instance.DATABASE_VERSION) {

    val singleton = Singleton.instance

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun checkAnswer(answer : LatLng) : Boolean {
        val distance = getDistanceFromLatLonInKm(answer, currentQuestionLocation())

        Log.v("Distance", distance.toString())
        Log.v("Radius", currentQuestionRadius().toString())
        return distance < currentQuestionRadius()
    }

    fun currentQuestionLocation() : LatLng{
        val db = readableDatabase
        val cursor = db.query(singleton.curentTableQuest, arrayOf(QuestDataBase.LAT, QuestDataBase.LNG), ID + " = " + singleton.nowQuestion, null, null, null, null)

        var lat = 0.0
        var lng = 0.0
        if (cursor.moveToFirst()) {
            lat = cursor.getDouble(cursor.getColumnIndex(QuestDataBase.LAT))
            lng = cursor.getDouble(cursor.getColumnIndex(QuestDataBase.LNG))
        }
        db.close()
        cursor.close()
        return LatLng(lat, lng)
    }

    private fun currentQuestionRadius() : Double {
        val db = readableDatabase
        val cursor = db.query(singleton.curentTableQuest, arrayOf(QuestDataBase.RADIUS), ID + " = " + singleton.nowQuestion, null, null, null, null)

        var ans = 0.001
        if (cursor.moveToFirst()) {
            ans *= cursor.getDouble(cursor.getColumnIndex(QuestDataBase.RADIUS)).toDouble()
        }
        db.close()
        cursor.close()
        return ans
    }

    private fun getDistanceFromLatLonInKm(place1: LatLng, place2: LatLng) : Double { // функция, высчитывающая расстояния
        val R = 6371 // Радиус Земли в км
        val dLat = deg2rad(place2.latitude-place1.latitude) // deg2rad находится ниже
        val dLon = deg2rad(place2.longitude-place1.longitude)
        val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(place1.latitude)) * Math.cos(deg2rad(place2.latitude)) *
                Math.sin(dLon/2) * Math.sin(dLon/2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
        val d = R * c // расстояние в км
        return Math.abs(d)
    }

    fun deg2rad(deg: Double): Double{ // переводит градусы в радианы
        return deg * (Math.PI/180)
    }

    fun getQuestion(number: Int) : String{
        val db = readableDatabase
        val cursor = db.query(singleton.curentTableQuest, arrayOf(QuestDataBase.QUESTION), ID + " = " + number, null, null, null, null)

        var question = "-1"
        if (cursor.moveToFirst()) {
            question = cursor.getString(cursor.getColumnIndex(QuestDataBase.QUESTION))
        }
        db.close()
        cursor.close()
        return question
    }

    fun getTip(number: Int) : String{
        val db = readableDatabase
        val cursor = db.query(singleton.curentTableQuest, arrayOf(QuestDataBase.TIP), ID + " = " + number, null, null, null, null)

        var tip = "-1"
        if (cursor.moveToFirst()) {
            tip = cursor.getString(cursor.getColumnIndex(QuestDataBase.TIP))
        }
        db.close()
        cursor.close()
        return tip
    }
    fun getNumberOfQuestions() : Int{
        val db = readableDatabase
        val cursor = db.query(singleton.curentTableQuest, null, null, null, null, null, null)
        val res = cursor.count
        db.close()
        cursor.close()
        return res
    }

    fun initDataBase(){
        Log.v("INITDATABASE", "start")
        val db = this.readableDatabase
        val contentValues = ContentValues()
        val allTable = AllQuestsDataBase.TABLE
        try {
            db.delete(allTable, null, null)
        }
        catch (e : Exception){}
        Log.v("INITDATABASE", "first try")
        db.execSQL(("create table if not exists " + allTable + " ( "
                + AllQuestsDataBase.ID + " INTEGER PRIMARY KEY, "
                + AllQuestsDataBase.LAT + " REAL, "
                + AllQuestsDataBase.LNG + " REAL, "
                + AllQuestsDataBase.TABLENAME +" TEXT, "
                + AllQuestsDataBase.QUESTSTARTTEXT + " TEXT "
                +");"))
        Log.v("INITDATABASE", "1table")
        var table : String = ""

        table = "quest1"
        contentValues.clear()
        contentValues.put(LAT, 59.980556)
        contentValues.put(LNG, 30.324234)
        contentValues.put(AllQuestsDataBase.TABLENAME, "quest1")
        contentValues.put(AllQuestsDataBase.QUESTSTARTTEXT, "И поэтому улыбайся...")
        db.insert(allTable, null, contentValues)

        try {
            db.delete(table, null, null)
        }
        catch (e : Exception){}
        db.execSQL(("create table if not exists " + table + " ( "
                + ID + " INTEGER PRIMARY KEY, "
                + QUESTION + " TEXT, "
                + TIP + " TEXT, "
                + LAT + " REAL, "
                + LNG + " REAL, "
                + RADIUS + " INTEGER "
                +");"))

        contentValues.clear()
        contentValues.put(QUESTION, "Акватория Финского залива...\nАкватория Невы...\nВсе они так недоступны для простого человека.\nНо вы-то знаете, что есть ещё одна Акватория, у которой даже есть вход для людей!")
        contentValues.put(TIP, "Обратите внимание на то, что слово Акватория написано с заглавной буквы")
        contentValues.put(LAT, 59.98025437989682)
        contentValues.put(LNG, 30.322864419731786)
        contentValues.put(RADIUS, 50)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Граф П.А. Строганов. В честь него многое названо - дворец, железнодорожная станция, сад рядом с метро Чёрная Речка.\n" +
                "А ещё в честь него назван небольшой парк, к которому ведёт пешеходный мост через Чёрную Речку.\n" +
                "Но этот мост вам не нужен. Вам нужен тот, что рядом, но по которому ни человек, ни машина проехать не смогут")
        contentValues.put(TIP, "Это не совсем мост, скорее трубопровод")
        contentValues.put(LAT, 59.98432686183018)
        contentValues.put(LNG, 30.312572783660926)
        contentValues.put(RADIUS, 100)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Москва - Деревня?\n" +
                "Но оказывается, не только она, но и часть нынешнего Санкт-Петербурга когда-то была деревней.\n" +
                "Все, конечно же, знают Старую Деревню, но, раз есть назвали 'Старая', значит, появилась 'Новая'\n" +
                "Сейчас есть одно глобальное напоминание об этой 'Новой'. Найдите его, и учтите, что переходить на красный - нельзя")
        contentValues.put(TIP, "Нужное вам место - некоторый большой транспортный объект")
        contentValues.put(LAT, 59.9927116738247)
        contentValues.put(LNG, 30.29946214599613)
        contentValues.put(RADIUS, 200)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Погиб поэт! - невольник чести - \n" +
                "Его убийцей был Дантес")
        contentValues.put(TIP, "А где же был убит Александр Сергеевич?")
        contentValues.put(LAT, 59.995028810239255)
        contentValues.put(LNG, 30.30191368511646)
        contentValues.put(RADIUS, 50)
        db.insert(table, null, contentValues)



        table = "quest2"
        contentValues.clear()
        contentValues.put(LAT, 59.927661)
        contentValues.put(LNG, 30.345923)
        contentValues.put(AllQuestsDataBase.TABLENAME, "quest2")
        contentValues.put(AllQuestsDataBase.QUESTSTARTTEXT, "По местам Достоевского")
        db.insert(allTable, null, contentValues)

        try {
            db.delete(table, null, null)
        }
        catch (e:Exception){}
        db.execSQL(("create table if not exists " + table + " ( "
                + ID + " INTEGER PRIMARY KEY, "
                + QUESTION + " TEXT, "
                + TIP + " TEXT, "
                + LAT + " REAL, "
                + LNG + " REAL, "
                + RADIUS + " INTEGER "
                +");"))

        contentValues.clear()
        contentValues.put(QUESTION, "В доме на ... жил Фёдор Достоевский, здесь же он скончался 28 января " +
                "(9 февраля) 1881 года. К 150-тилетию со дня рождения писателя, которое отпраздновали в 1971 году, в этом месте открылся музей")
        contentValues.put(TIP, "В доме на Кузнечном переулке жил Фёдор Достоевский")
        contentValues.put(LAT, 59.927419)
        contentValues.put(LNG, 30.350546)
        contentValues.put(RADIUS, 75)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Отрывок из романа 'Идиот': 'Дом этот был большой, мрачный, в три этажа, без всякой архитектуры, " +
                "цвету грязно-зелёного. Некоторые, очень, впрочем, немногие дома в этом роде, выстроенные в конце прошлого столетия, уцелели " +
                "именно в этих улицах Петербурга (в котором всё так скоро меняется) почти без перемен'")
        contentValues.put(TIP, "Именно этот дом на Гороховой улице считают домом Рогожина")
        contentValues.put(LAT, 59.928909)
        contentValues.put(LNG, 30.321239)
        contentValues.put(RADIUS, 100)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "'Все торговцы на столах, на лотках, в лавках и лавочках запирали свои заведения или снимали " +
                "и прибирали свой товар и расходились по домам, равно как и их покупатели. Около харчевен в нижних этажах, на грязных " +
                "и вонючих дворах домов, а наиболее у распивочных, толпилось много разного и всякого сорта промышленников и лохмотников'")
        contentValues.put(TIP, "В 1930-е годы эти места подверглись реконструкции: территорию благоустроили, рынок снесли. В наше время это место" +
                " совсем не похоже на то, что описывал Ф.М. Достоевский")
        contentValues.put(LAT, 59.926796)
        contentValues.put(LNG, 30.317838)
        contentValues.put(RADIUS, 300)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "'Каморка его приходилась самой кровлей высокого пятиэтажного дома и походила более на шкаф, чем на квартиру. " +
                "Квартирная же хозяйка его, у которой он нанимал эту каморку с обедом и прислугой, помещалась одною лестницей ниже, " +
                "в отдельной квартире, и каждый раз, при выходе на улицу, ему непременно надо было пройти мимо хозяйкиной кухни, почти всегда отворённой на лестницу'")
        contentValues.put(TIP, "речь конечно же идёт о доме Раскольникова")
        contentValues.put(LAT, 59.927692)
        contentValues.put(LNG, 30.311023)
        contentValues.put(RADIUS, 100)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Здесь начинается действие романа Ф.М. Достоевского 'Преступление и наказание': '" +
                "В начале июля, в чрезвычайно жаркое время, под вечер, один молодой человек вышел из своей каморки, которую " +
                "нанимал от жильцов в С-м переулке, на улицу и медленно, как бы в нерешимости, отправился к ... мосту'")
        contentValues.put(TIP, "В оригинальном тексте: '... отправился к К-ну мосту'")
        contentValues.put(LAT, 59.925828)
        contentValues.put(LNG, 30.313643)
        contentValues.put(RADIUS, 75)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "По мнению исследователей творчества Ф.М. Достоевского, в этот дом писатель поселил героиню старуху-процентщицу, Алёну Ивановну - '" +
                "кроршечную, сухую старушонку, лет шестидесяти, с вострыми и злыми глазками, с маленьким вострым носом и простоволосую'")
        contentValues.put(TIP, "Изучите внимательно набережную канала Грибоедова")
        contentValues.put(LAT, 59.924599)
        contentValues.put(LNG, 30.303217)
        contentValues.put(RADIUS, 100)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "2 декабря 1849 года в 8 часов утра на ... привели арестантов, в их числе был и Достоевский. К этому времени " +
                "на плацу возвели эшафот из дерева, который заранее обтянули чёрным сукном. " +
                "После команды 'строить шеренгу' арестантов выстраивают в ряд и ведут к эшафоту. О каком событии идёт речь? Где разворачивались описанные события?")
        contentValues.put(TIP, "Речь идёт о казни петрашевцев на Семёновском плацу")
        contentValues.put(LAT, 59.921145)
        contentValues.put(LNG, 30.333012)
        contentValues.put(RADIUS, 250)
        db.insert(table, null, contentValues)

        table = "quest3"
        contentValues.clear()
        contentValues.put(LAT, 59.979225)
        contentValues.put(LNG, 30.269985)
        contentValues.put(AllQuestsDataBase.TABLENAME, table)
        contentValues.put(AllQuestsDataBase.QUESTSTARTTEXT, "Дворцы и замки Санкт-Петербурга")
        db.insert(allTable, null, contentValues)

        try {
            db.delete(table, null, null)
        }
        catch (e:Exception){}
        db.execSQL(("create table if not exists " + table + " ( "
                + ID + " INTEGER PRIMARY KEY, "
                + QUESTION + " TEXT, "
                + TIP + " TEXT, "
                + LAT + " REAL, "
                + LNG + " REAL, "
                + RADIUS + " INTEGER "
                +");"))

        contentValues.clear()
        contentValues.put(QUESTION, "Один из дворцов Санкт-Петербурга, памятник времен Екатерины II, загородная императорская резиденция. " +
                "Расположен на одном из многочсленных островов Санкт-Петербурга")
        contentValues.put(TIP, "Сейчас там расположилась 'Академия талантов'")
        contentValues.put(LAT, 59.979774)
        contentValues.put(LNG, 30.304516)
        contentValues.put(RADIUS, 300)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Этот дворец был построен по указанию Екатерины II для своего фаворита, светлейшего князя Потёмкина. " +
                "На возведение и отделку дворца было затрачено около 400 000 рублей золотом. " +
                "Дворец получил своё название по титулу князя ..., который был пожалован временщику в 1787 году, " +
                "после присоединения Крым к Российской Империи")
        contentValues.put(TIP, "Крым иногда называют Тавридой")
        contentValues.put(LAT, 59.948292)
        contentValues.put(LNG, 30.375981)
        contentValues.put(RADIUS, 250)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Дворец был построен в стиле Петровского барокко по проекту Трезини. " +
                "Это одно из старейших зданий города")
        contentValues.put(TIP, "Туристы приходят посмотреть не столько сам дворец, сколько сад, в котором " +
                "он расположен, и решётку, ограждающую этот сад")
        contentValues.put(LAT, 59.947328)
        contentValues.put(LNG, 30.335962)
        contentValues.put(RADIUS, 250)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Про это место ходит много легенд, одна из них гласит, что " +
                "призрак убитого заговорщиками императора не смог покинуть место своей смерти " +
                "и до сих пор обитает в стенах этого здания")
        contentValues.put(TIP, "Речь идёт об единственном замке в городе")
        contentValues.put(LAT, 59.940362)
        contentValues.put(LNG, 30.337831)
        contentValues.put(RADIUS, 250)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Один из императорских дворцов. Получил своё название в честь моста, располоэенного рядом")
        contentValues.put(TIP, "На этом мосту расположились 4 скульптуры, изображающие укрощение коней")
        contentValues.put(LAT, 59.932934)
        contentValues.put(LNG, 30.340344)
        contentValues.put(RADIUS, 250)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Символ и визитная карточка Культурной столицы России")
        contentValues.put(TIP, "Заглянги на Дворцовую площадь, может увидиш что, или вспомнишь")
        contentValues.put(LAT, 59.940310)
        contentValues.put(LNG, 30.314032)
        contentValues.put(RADIUS, 300)
        db.insert(table, null, contentValues)


        table = "quest4"
        contentValues.clear()
        contentValues.put(LAT, 59.884297)
        contentValues.put(LNG, 29.911035)
        contentValues.put(AllQuestsDataBase.TABLENAME, table)
        contentValues.put(AllQuestsDataBase.QUESTSTARTTEXT, "Нижний парк Петергофа")
        db.insert(allTable, null, contentValues)

        try {
            db.delete(table, null, null)
        }
        catch (e:Exception){}
        db.execSQL(("create table if not exists " + table + " ( "
                + ID + " INTEGER PRIMARY KEY, "
                + QUESTION + " TEXT, "
                + TIP + " TEXT, "
                + LAT + " REAL, "
                + LNG + " REAL, "
                + RADIUS + " INTEGER "
                +");"))

        contentValues.clear()
        contentValues.put(QUESTION, "Считается, что на его создание повлияло увлечение двора Китаем, что " +
                "отразилось также в возведении \"китайских\" построек в Царском Селе. В 60-х годах 20 века" +
                " кровле фонтана придали облик гриба-мухомора. Этот фонтан-шутиха вам и нужен.")
        contentValues.put(TIP, "Фонтан имеет форму зонта ")
        contentValues.put(LAT, 59.885079)
        contentValues.put(LNG, 29.918769)
        contentValues.put(RADIUS, 50)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "До Нижнего парка Петергофа можно добраться на автобусе, " +
                "электричке, метеоре по заливу. Мало кто знает, что в парке нередко приземляется " +
                "вертолет. Вам нужно на место его остановки.")
        contentValues.put(TIP, "Ищите на берегу залива в западной части парка")
        contentValues.put(LAT, 59.888709)
        contentValues.put(LNG, 29.896570)
        contentValues.put(RADIUS, 75)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Этот дворец получил своё имя в память о посещении Петром I в " +
                "1717 резиденции французских королей в Марли-ле-Руа под Парижем. Подойдите ко " +
                "дворцу со стороны большего из окружающих его прудов")
        contentValues.put(TIP, "Внимательно посмотри на карту - дворец окружён прудами")
        contentValues.put(LAT, 59.888709)
        contentValues.put(LNG, 29.896570)
        contentValues.put(RADIUS, 75)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Следуя по аллее вдоль берега Марлинского пруда, посетитель парка" +
                " невольно останавливается перед обширной площадкой с каскадом и двумя высокими фонтанами." +
                " Подойдите к тому из них, который ближе к Самсону.")
        contentValues.put(TIP, "Высокий каскад с 22 мраморными ступеньками")
        contentValues.put(LAT, 59.887368)
        contentValues.put(LNG, 29.897406)
        contentValues.put(RADIUS, 100)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "В центре композиции этого фонтана расположена скульптура Тритона," +
                " борющегося с морским чудовищем, изображенным в виде крокодила с рыбьим хвостом. Из" +
                " его пасти вырывается водяная струя высотой в восемь метров. Четыре черепахи, вытянув " +
                "от страха шеи, расползаются в разные стороны. Из их ртов также бьют клокочущие струи воды.")
        contentValues.put(TIP, "фонтан \"Оранжерейный\"")
        contentValues.put(LAT, 59.884248)
        contentValues.put(LNG, 29.913753)
        contentValues.put(RADIUS, 50)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "В мае 2001 года на Монплезирской аллее Нижнего парка, неподалеку " +
                "от Римских фонтанов, появилась шутиха, которая не работала 280 лет. Она была сооружена" +
                " по затее Петра I архитектором Н. Микетти, а первый пуск состоялся в 1721 году. " +
                "По царскому замыслу значительная часть аллеи внезапно перекрывалась водяной аркой " +
                "из трехсот струй, и все гуляющие оказывались мокрыми с головы до пят. Неслучайно " +
                "шутиху в петровское время называли \"Мочильной дорогой\". Однако вскоре после " +
                "открытия фонтан исчез. Видимо, затея оказалась слишком грубой.")
        contentValues.put(TIP, "Рядом с шутихой \"Зонтиком\"")
        contentValues.put(LAT, 59.884386)
        contentValues.put(LNG, 29.917390)
        contentValues.put(RADIUS, 50)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "Вопрос: Их внешний облик скопирован с двух фонтанов," +
                " установленных в Риме, на площади перед собором Святого Петра. Из-за этого фонтаны" +
                " получили такое название. Вам нужно место между этими фонтанами.")
        contentValues.put(TIP, "Вам нужны Римские фонтаны ")
        contentValues.put(LAT, 59.884103)
        contentValues.put(LNG, 29.917059)
        contentValues.put(RADIUS, 75)
        db.insert(table, null, contentValues)

        contentValues.clear()
        contentValues.put(QUESTION, "В восточной части парка сохранились следы планировки Лабиринта." +
                " Он представлял собой квадратный участок площадью около 2 гектаров. В центре, куда " +
                "вам нужно попасть, находился проточный овальный бассейн. От площади вокруг бассейна " +
                "расходились восемь дорожек. Их пересекала кольцевая аллея, разделявшая участок на " +
                "шестнадцать куртин, в которых высаживали цветы.")
        contentValues.put(TIP, "Поищите на берегу Финского залива")
        contentValues.put(LAT, 59.884529)
        contentValues.put(LNG, 29.929031)
        contentValues.put(RADIUS, 75)
        db.insert(table, null, contentValues)


        db.close()
    }

    companion object {

        val TIP = "tip"
        val ID = "id"
        val QUESTION = "question"
        val LAT = "lat"
        val LNG = "lng"
        val RADIUS = "radius"
        val TABLENAME = "tableName"
    }
}
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
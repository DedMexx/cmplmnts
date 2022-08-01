require: slotfilling/slotFilling.sc
  module = sys.zb-common

require: dateTime/dateTime.sc
  module = sys.zb-common

require: patterns.sc
  module = sys.zb-common
  
require: localPatterns.sc

require: func.js

init:
    bind("postProcess", function($context) {
        $context.session.lastState = $context.currentState;
    })

theme: /
    state: Welcome
        q!: $regex</start>
        q!: *start
        q: $hello || fromState = /Remind, onlyThisState = true
        script:
            var timestamp = moment($jsapi.currentTime());
            var hour = Number(timestamp.format("HH")) + 2; // Прибавляем до московского времени (почему-то изначально на 2 часа меньше)
            if (hour >= 5 && hour < 11) {
                $temp.goodTimeOfDay = 'Доброе утро!';     
            }
            else if (hour >= 11 && hour < 17) {
                $temp.goodTimeOfDay = 'Добрый день!';
            }
            else if (hour >= 17 && hour < 23) {
                $temp.goodTimeOfDay = 'Добрый вечер!';
            }
            else {
                $temp.goodTimeOfDay = 'Доброй ночи!';     
            }
        random:
            a: Здравствуй!
            a: Привет!
            a: {{$temp.goodTimeOfDay}}
            a: Доброго времени суток!
        if: !$client.heKnowMe
            go!: /Remind/Description
        else:
            go!: /Remind
        script:
            $client.heKnowMe = true;
            
    state: Remind || modal = true
        a: Напомнить тебе, кто я, или перейдём к работе?
        buttons:
            "Напомни кто ты" -> /Remind/Description
            "Перейти к работе" -> /Remind/StartWork
        state: Description
            q: $whoYou
            a: {{$injector.botDescription}}
            a: Ну что, начнём?
            buttons:
                "Перейти к работе" -> /Remind/StartWork
        state: StartWork
            q: $startWork
            a: Хорошо, начнём!
            go!: /TypeChoice
    
    state: TypeChoice
        q: $typeChoice
        a: Для начала выбери, ты хочешь:
            1 - Анекдот
            2 - Рассказ
            3 - Стишок
            4 - Афоризм
            5 - Цитату
            6 - Тост
            7 - Статус
        buttons:
            "Анекдот"
            "Рассказ"
            "Стишок"
            "Афоризм"
            "Цитату"
            "Тост"
            "Статус"
    state: CatchAll || noContext = true
        event!: noMatch
        a: Извините, я вас не понял. Переформулируйте, пожалуйста.
        go!: {{$session.lastState}}
    
    state: CatchBadWords || noContext = true
        q!: * $obsceneWord *
        script:
            $response.replies = $response.replies || [];
            $response.replies.push({
                type: "image",
                imageUrl: "https://fikiwiki.com/uploads/posts/2022-02/1644818975_36-fikiwiki-com-p-kartinki-ti-ne-khochesh-so-mnoi-obshchatsy-39.jpg",
                text: "Пожалуйста, не обижайте меня, я ведь хочу вам помочь :)"
            });
        go!: {{$session.lastState}}
        
        
        
        # script: 
        #     $temp.somethingFun = getSomethingFun(11);
        # if: $temp.somethingFun
        #     a: {{$temp.somethingFun}}
        # else:
        #     script:
        #         $jsapi.startSession();
        #         $response.replies = $response.replies || [];
        #         $response.replies.push({
        #             type: "image",
        #             imageUrl: "https://i.ytimg.com/vi/JqLz0ULmGUY/maxresdefault.jpg",
        #             text: "К сожалению, в данный момент наш сервис поиска комплиментов не работает. Приносим свои извинения."
        #         });
    
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
        q!: $hello
        q: $hello
        script:
            $jsapi.startSession();
            var timestamp = moment($jsapi.currentTime());
            var hour = Number(timestamp.format("HH")) + 3; // Прибавляем до московского времени (почему-то изначально на 3 часа меньше)
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
            
    state: Remind
        a: Напомнить тебе, кто я, или перейдём к работе?
        buttons:
            "Напомни кто ты" -> /Remind/Description
            "Перейти к подбору" -> /Remind/StartWork
        state: Description
            q!: $whoYou
            a: {{$injector.botDescription}}
            random:
                a: Итак, давай начнём?
                a: Ну что, начнём?
                a: Что ж, можем начать?
            script:
                $client.heKnowMe = true;
            buttons:
                "Перейти к подбору" -> /TypeChoice
            q: $typeChoice || toState = /TypeChoice, onlyThisState = true
            q: $no || toState = /Rejected, onlyThisState = true
        state: StartWork
            q: $startWork
            random:
                a: Хорошо, начнём!
                a: Поехали!
                a: Хорошо
            go!: /TypeChoice
            
    #Стейт для "Нет"
    state: /Rejected 
        random:
            a: Тогда, боюсь, я больше ничем не могу тебе помочь
            a: Хорошо, но ты можешь писать мне когда хочешь!
            a: Ладно, но я буду ждать твоего сообщения!
            a: Окей, но я надеюсь, что я ещё смогу поднять тебе настроение!
        script:
            $jsapi.stopSession();
            
    state: TypeChoice
        q: $yesNew || fromState = /Result, onlyThisState = true
        a: Для начала выбери, что ты хочешь:
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
            
    state: Okey
        q: $whatYouWant
        script:
            $session.choice = $parseTree._whatYouWant;
        random:
            a: Хорошо
            a: Понял
        go!: /NormalOrAdult
        
    state: NormalOrAdult
        a: Ты хочешь с приличием или 18+?
        buttons:
            "С приличием"
            "18+"
            
    state: Result
        q: $normalOrAdult
        random:
            a: Готово:
            a: Хорошо, вот:
        script: 
            $session.normalOrAdult = $parseTree._normalOrAdult;
            var arrayOfTypes = ['анекдот', 'рассказ', 'стишок', 'афоризм', 'цитату', 'тост', 'статус'];
            var normal = ['с приличием', 'приличный', 'детский', 'с цензурой', 'цензурный'];
            var adult = ['взрослый', '18+', 'без приличия', 'без цензуры', 'бесцензурный'];
            $context.session.whatType = arrayOfTypes.indexOf($session.choice.toLowerCase()) + 1;
            if (Number($session.choice) == $session.choice) {
                $context.session.whatType = Number($session.choice); 
            }
            if ($context.session.whatType == 7) {
                $context.session.whatType += 1;
            }
            if (normal.indexOf($session.normalOrAdult.toLowerCase()) == -1){
                $context.session.whatType += 10;
            }
            log($context.session.whatType);
            $temp.somethingFun = getSomethingFun($context.session.whatType);
        if: $temp.somethingFun
            a: {{$temp.somethingFun}}
            random:
                a: Попробовать подобрать тебе снова?
                a: Хочешь, чтоб я что нибудь подобрал?
                a: Давай я попробую снова помочь тебе?
            buttons:
                "Да!" -> /TypeChoice
                "Нет" -> /Bye
            script:
                $jsapi.stopSession();
        else:
            script:
                $response.replies = $response.replies || [];
                $response.replies.push({
                    type: "image",
                    imageUrl: "https://i.ytimg.com/vi/JqLz0ULmGUY/maxresdefault.jpg",
                    text: "К сожалению, в данный момент наш сервис поиска комплиментов не работает, попробуйте позже. Приносим свои извинения."
                });
               $jsapi.stopSession();
        
    state: CatchAll || noContext = true
        event!: noMatch
        random:
            a: Извини, я тебя не понял. Переформулируй, пожалуйста
            a: Не понимаю тебя. Скажи по другому, пожалуйста
            a: Не смог уяснить. Перефразируй, пожалуйста
        go!: {{$session.lastState}}
    
    state: CatchBadWords || noContext = true
        q!: * $obsceneWord *
        script:
            $response.replies = $response.replies || [];
            $response.replies.push({
                type: "image",
                imageUrl: "https://fikiwiki.com/uploads/posts/2022-02/1644818975_36-fikiwiki-com-p-kartinki-ti-ne-khochesh-so-mnoi-obshchatsy-39.jpg",
                text: "Пожалуйста, не обижай меня, я ведь хочу тебе помочь :)"
            });
        go!: {{$session.lastState}}
    
    state: Bye || noContext = true
        q: $no || fromState = /Result, onlyThisState = true
        q!: $bye
        random:
            a: Пока!
            a: До новых встреч!
            a: Удачи в твоих делах!
            a: Хорошо, пиши мне ещё!
        script:
            $jsapi.stopSession();
    
    state: What || noContext = true
        q!: $what
        random:
            a: Повторю:
            a: Смотри:
            a: Ещё раз:
        go!: {{$session.lastState}}
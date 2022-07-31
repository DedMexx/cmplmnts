require: slotfilling/slotFilling.sc
  module = sys.zb-common

require: dateTime/dateTime.sc
  module = sys.zb-common

require: patterns.sc
  module = sys.zb-common
  
require: localPatterns.sc

require: func.js

theme: /
    state: Welcome
        q!: $regex</start>
        q!: *start
        q!: $hello
        script:
            var timestamp = moment($jsapi.currentTime());
            var hour = Number(timestamp.format("HH"));
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
        a: {{$temp.goodTimeOfDay}}
        # random:
        #     a: Здравствуй!
        #     a: Привет!
            
            
        script: 
            $temp.somethingFun = getSomethingFun(11);
        if: $temp.somethingFun
            a: {{$temp.somethingFun}}
        else:
            script:
                $jsapi.startSession();
                $response.replies = $response.replies || [];
                $response.replies.push({
                    type: "image",
                    imageUrl: "https://i.ytimg.com/vi/JqLz0ULmGUY/maxresdefault.jpg",
                    text: "К сожалению, в данный момент наш сервис поиска комплиментов не работает. Приносим свои извинения."
                });
    
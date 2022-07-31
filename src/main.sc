require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: func.js

theme: /
    state: Welcome
        q!: $regex</start>
        q!: *start
        q!: (прив*/здравств*/здаров*)
        a: Привет!
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
    
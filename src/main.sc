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
            $temp.compliment = getCompliment();
        a: {{$temp.compliment.compliment}}
    
require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: func.js
theme: /
    state: /start
        q!: прив
        a: Привет!
        script:
            $temp.compliment = getCompliment();
        a: {{$temp.compliment}}
    
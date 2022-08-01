function getSomethingFun(index) {
    var response = $http.get('http://rzhunemogu.ru/RandJSON.aspx?CType=${index}', {
        timeout: 12000,
        query: {
            index: index
        }
    });
    if (!response.isOk || !response.data) {
        return false;
    }
    try {
        var result = response.data.slice(12, -2); 
    } catch (RuntimeException) {
        var result = 'Извините, произошла ошибка, попробуйте снова';
    }
    return result;

    /* По какой то причине .content не работает (выводит пустую строчку, 
    поэтому решил возвращать объект, обрезав лишнее) */
    
    
    /* Также почему-то иногда выдаёт ошибку (Судя по всему из-за недопустимых символов)
    "RuntimeException: java.util.concurrent.ExecutionException: 
    com.justai.zb.scenarios.errors.ScriptingException: src/func.js:11 
    TypeError: response.data.slice is not a function" */
}

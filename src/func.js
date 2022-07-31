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
    return response;
}

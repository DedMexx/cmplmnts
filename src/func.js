// function getCompliment() {
// 	var xhr = new XMLHttpRequest();
// 	xhr.open('GET', 'https://complimentr.com/api', false);
// 	xhr.send();
// 	if (xhr.status != 200) {
// 		return 'Извините, но в данный момент наш сервис по генерации комплиментов недоступен'
// 	}
// 	else { return xhr.response.slice(15, -2); }
// }
function getCompliment() {
    // var response = $http.get("https://complimentr.com/api", {timeout: 15000});
    // if (!response.isOk || !response.data) {
    //     return false;
    // }
    var response = $http.get("https://complimentr.com/api");
    return response.compliment;
}

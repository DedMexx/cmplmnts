function getCompliment() {
	var xhr = new XMLHttpRequest();
	xhr.open('GET', 'https://complimentr.com/api', false);
	xhr.send();
	if (xhr.status != 200) {
		return 'Извините, но в данный момент наш сервис по генерации комплиментов недоступен'
	}
	else { return xhr.response.slice(15, -2); }
}

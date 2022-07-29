function getCompliment() {
    var response = $http.get("https://complimentr.com/api", {
        timeout: 15000
    });
    
    if (!response.isOk || !response.data) {
        return false;
    }
    
    var compliment = response.comliment;
    return compliment;
}
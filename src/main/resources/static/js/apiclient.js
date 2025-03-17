var apiclient = (function () {
    const API_URL = "https://your-api-url.com/blueprints"; // Reemplaza con la URL real de tu API

    function getBlueprintsByAuthor(author, callback) {
        $.get(`${API_URL}/${author}`)
            .done(function (data) {
                console.log("Blueprints retrieved:", data);
                callback(data);
            })
            .fail(function (error) {
                console.error("Error fetching blueprints:", error);
            });
    }

    function getBlueprintsByNameAndAuthor(author, blueprintName, callback) {
        $.get(`${API_URL}/${author}/${blueprintName}`)
            .done(function (data) {
                console.log("Blueprint retrieved:", data);
                callback(data);
            })
            .fail(function (error) {
                console.error("Error fetching blueprint:", error);
            });
    }

    return {
        getBlueprintsByAuthor: getBlueprintsByAuthor,
        getBlueprintsByNameAndAuthor: getBlueprintsByNameAndAuthor
    };
})();

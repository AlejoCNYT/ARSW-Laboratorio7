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

    function updateBlueprint(author, blueprintName, points, callback) {
        let blueprintData = { name: blueprintName, points: points };

        $.ajax({
            url: `${API_URL}/${author}/${blueprintName}`,
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(blueprintData)
        })
            .done(function (data) {
                console.log("Blueprint updated successfully:", data);
                callback(data);
            })
            .fail(function (error) {
                console.error("Error updating blueprint:", error);
            });
    }

    return {
        getBlueprintsByAuthor: getBlueprintsByAuthor,
        getBlueprintsByNameAndAuthor: getBlueprintsByNameAndAuthor,
        updateBlueprint: updateBlueprint
    };
})();

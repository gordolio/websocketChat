
(function(){
    Handlebars.registerHelper("math", function(value1, operator, value2)
    {
        if(operator === '+') {
            return parseInt(value1) + parseInt(value2);
        } else if(operator === '-') {
            return parseInt(value1) - parseInt(value2);
        } else if(operator === '*') {
            return parseInt(value1) * parseInt(value2);
        } else if(operator === '/') {
            return parseInt(value1) / parseInt(value2);
        }
    });

    Handlebars.registerHelper("set", function(key,value,options) {
        options.data.root[key] = value;
    });

})();

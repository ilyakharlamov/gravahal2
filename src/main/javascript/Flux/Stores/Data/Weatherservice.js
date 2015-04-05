/*global */
const $ = require('jQuery');
export default class Weatherservice {
  getWeather (cityquery) {
    var d = new $.Deferred();
    $.ajax({
      url: `http://api.openweathermap.org/data/2.5/weather?q=${cityquery}&units=metric`,
    }).done((response) => {
      if (!response || !response.cod || !response.cod===200 || !response.weather || !response.weather.length) {
        d.reject({
          cityquery: cityquery,
        });
        return d.promise();
      }
      d.resolve({
        cityname: response.name,
        icon: `http://openweathermap.org/img/w/${response.weather[0].icon}.png`,
        temp: response.main.temp,
      });
    }).fail( () => {
      d.reject({});
    });
    return d.promise();
  }
}

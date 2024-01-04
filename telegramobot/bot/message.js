const {bot} = require('./bot')
const {start} = require("./helper/start");
const {get_all_movies} = require("./helper/kinolar");
const {get_all_movie_for_subtitle} = require('./helper/subtitle')
const {get_all_serials} = require("./helper/serial");
const {editOnOf} = require("./helper/developer");


bot.on('message', async msg => {
    const chatId = msg.from.id
    const text = msg.text
    if (text === '/start') {
        await start(chatId)
    }
    if (text === 'Kinolar') {
        await get_all_movies(chatId)
    }
    if (text === 'Subtitlelar') {
        await get_all_movie_for_subtitle(chatId)
    }
    if (text === 'Seriallar') {
        await get_all_serials(chatId)
    }
    if (text === 'DeveloperModeEditor') {
        await editOnOf(chatId)
    }
})

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Bot extends TelegramLongPollingBot {

    // String PATH_NAME = "src/main/resources/";

    SQLHandler sqlHandler = new SQLHandler();



    public static void main(String[] args) {



        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "Бот Льда и Пламени";
    }

    @Override
    public String getBotToken() {
        return "722233451:AAFpv4KgNDISqgxX4cZYJDpNsQf5A5ORiII";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();


        if (message != null && message.hasText()) {
            sqlHandler.connect();
            if (!sqlHandler.isUserRegistered(message.getChatId())) {
                sqlHandler.registerUser(message.getChatId(),message.getChat().getFirstName(),message.getChat().getUserName());
                // sqlHandler.disconnect();
            }
                sendMsg(message);



        }
    }


    private void sendMsg(Message message) {

        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId().toString());
        SendPhoto sendPhoto = new SendPhoto().setChatId(message.getChatId().toString());
        sendMessage.enableHtml(false);
        sendMessage.enableMarkdown(false);

        if (message.getText().equals("/add")){
            if (sqlHandler.isQuoteAlreadyFound(message.getChatId(),1)){
                sendMessage.setText("Уже было");
            }
            else {
                sqlHandler.addFoundedQuote(message.getChatId(), 1);
                sendMessage.setText("Отлично");
            }

        }

        if (message.getText().startsWith("/start")) {

            sendMessage.setText("Привет, "+message.getChat().getFirstName()+"\nПравила игры: \n" +
                    "Нужно вспомнить как можно больше стран. Вводить нужно сокращенное название на английском языке из двух букв, например RU\n" +
                    "Если будет введено верное значение, то Вы увидите флаг этой страны и получите 1 золотую монету\n" +
                    "Если будет введено неверное значение, Вам придет в ответ пиратский флаг и Вы потеряете 1 золотую монету\n" +
                    "Удачной игры!" +
                    "\nБольшая просьба добавить себе ник в telegram если его еще нет" +
                    "\nПосмотреть топ 10 - /top");


        }


        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }

//        else if (message.getText().equals("/top")) {
//            try {
//                execute(sendMessage.setText(sqlHandler.top10()));
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }


        }
    }
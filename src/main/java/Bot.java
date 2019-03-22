
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.io.File;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


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
        return "xxx";
    }

    @Override
    public void onUpdateReceived(Update update) {
        sqlHandler.connect();
        Handler handler = new ConsoleHandler();
        Logger logger = Logger.getLogger(Bot.class.getName());
        handler.setLevel(Level.FINE);
        logger.setLevel(Level.FINE);
        logger.addHandler(handler);
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                logger.log(Level.FINE, update.getMessage().getChat().getFirstName() + " -> " + update.getMessage().getText());
                if (update.getMessage().getText().equals("/start")) {
                    //sendMsg(update.getMessage().getChatId(),sqlHandler.getQuote(update.getMessage().getChatId())[0]);
                    try {
                        ArrayList<String>  list = sqlHandler.getQuote(update.getMessage().getChatId());
                        String quote = list.get(0);
                        list.remove(0);
                        Collections.shuffle(list);
                        execute(sendInlineButtons(update.getMessage().getChatId(),quote,list.get(1),list.get(2),list.get(3),list.get(0)));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                }

            }
        }
    }
//    private void sendMsg(long chatId,String text){
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText(text);
//        sendMessage.setChatId(chatId);
//
//       // sendInlineButtons(chatId,sqlHandler.getQuote(chatId)[1],sqlHandler.getQuote(chatId)[2],sqlHandler.getQuote(chatId)[3],sqlHandler.getQuote(chatId)[4]);
//
//    }

    private SendMessage sendInlineButtons(long chatId,String quote,String ans1,String ans2,String ans3,String ans4){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton().setText(ans1));
        keyboardRow1.add(new KeyboardButton().setText(ans2));
        keyboardRow2.add(new KeyboardButton().setText(ans3));
        keyboardRow2.add(new KeyboardButton().setText(ans4));

        list.add(keyboardRow1);
        list.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(list);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return new SendMessage().setChatId(chatId).setReplyMarkup(replyKeyboardMarkup).setText(quote);

    }



    }
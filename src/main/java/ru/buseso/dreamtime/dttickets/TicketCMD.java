package ru.buseso.dreamtime.dttickets;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import ru.buseso.dreamtime.dttickets.utils.BCypt;
import ru.buseso.dreamtime.dttickets.utils.TicketManager;

import java.util.Random;

public class TicketCMD extends Command {
    public TicketCMD(String name) { super(name); }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer)sender;
            if (args.length == 0) {
                p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix +
                        "/ticket create <почта> <пароль> - создать аккаунт в тикетах"));
                p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix +
                        "/ticket recovery - восстановить пароль от тикетов"));
                if(p.hasPermission("dttickets.admin")) {
                    p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix +
                            "/ticket admin <ник> - восстановить пароль от тикетов"));
                }
            } else if (args[0].equalsIgnoreCase("create")) {
                if (!TicketManager.hasAccount(p.getUniqueId().toString())) {
                    if (args.length == 3) {
                        String Email = args[1];
                        String Password = args[2];
                        if (isValidEmail(Email)) {
                            if (Password.length() > 5) {
                                String Hash = BCypt.hashpw(Password, BCypt.gensalt());
                                TicketManager.createAccount(p.getUniqueId().toString(), p.getName(), Hash, Email);
                                p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix + "§aАккаунт был успешно создан!"));
                            } else {
                                p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix + "§cИспользуйте как минимум 6 символов в пароле!"));
                            }
                        } else {
                            p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix + "§cНеправильная почта!"));
                        }
                    } else {
                        p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix + "§cВведите почту и пароль!"));
                    }
                } else {
                    p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix + "§cУ Вас уже есть аккаунт!"));
                    p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix + "§aЕсли Вы хотите восстановить пароль, напишите /ticket recovery"));
                }
            } else if (args[0].equalsIgnoreCase("recovery")) {
                if (!TicketManager.hasAccount(p.getUniqueId().toString())) {
                    p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix + "§cУ Вас ещё нет аккаунта!"));
                    p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix + "§aСоздайте его сейчас через /ticket create"));
                } else {
                    String newpass = newPass();
                    String hash = BCypt.hashpw(newpass, BCypt.gensalt());
                    TicketManager.updatePassword(p.getUniqueId().toString(),hash);

                    TextComponent text = new TextComponent(TextComponent.fromLegacyText(DTTickets.Prefix + "§aВы сгенерировали новый пароль: §6"+newpass+" §8(нажмите, чтобы скопировать его)"));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, newpass));
                    p.sendMessage(text);
                    p.sendMessage(TextComponent.fromLegacyText("§aИзменить пароль на свой Вы можете в настройках на сайте!"));
                }
            } else if (args[0].equalsIgnoreCase("admin")) {
                if(!TicketManager.hasAccountByName(args[1])) {
                    p.sendMessage(TextComponent.fromLegacyText(DTTickets.Prefix + "§cИгрок не имеет аккаунта!"));
                } else {
                    String name = args[1];
                    String pass = newPass();
                    String hash = BCypt.hashpw(pass, BCypt.gensalt());
                    TicketManager.updateOtherPassword(name, hash);

                    TextComponent text = new TextComponent(TextComponent.fromLegacyText(DTTickets.Prefix + "§aВы сгенерировали новый пароль для §2"+name+"§a: §6"+pass));
                    text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, pass));
                    p.sendMessage(text);
                }
            }
        }
    }

    static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    static String newPass() {
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder rndString = new StringBuilder(6);
        for(int i = 0; i < 6; i++) {
            int rnd = new Random().nextInt(symbols.length());
            rndString.append(symbols.charAt(rnd));
        }

        return rndString.toString();
    }
}

package md2html;

import java.util.ArrayList;

public class TextParser {
    ArrayList<String> arrayList = new ArrayList<>();
    private final String text;
    int pos = 0;

    TextParser(String text) {
        this.text = text;
    }

    public void makeHTML(StringBuilder out) {
        StringBuilder body = new StringBuilder();

        while (pos < text.length()) {
            switch (text.charAt(pos)){
                case '`', '~' ->  body.append(parseTeg(String.valueOf(text.charAt(pos))));

                case '*', '_' -> {
                    if (pos < text.length() - 1 && text.charAt(pos + 1) == text.charAt(pos)) {
                        pos++;
                        body.append(parseTeg(String.valueOf(text.charAt(pos)) + text.charAt(pos)));
                    } else {
                        body.append(parseTeg(String.valueOf(text.charAt(pos))));
                    }
                }

                case '-' -> {
                    if (pos < text.length() - 1 && text.charAt(pos + 1) == text.charAt(pos)) {
                        pos++;
                        body.append(parseTeg(String.valueOf(text.charAt(pos)) + text.charAt(pos)));
                    } else {
                        body.append(text.charAt(pos));
                    }
                }

                case '<' -> body.append("&lt;");
                case '>' -> body.append("&gt;");
                case '&' -> body.append("&amp;");

                case '\\' -> {
                    if (pos < text.length() - 1 && (text.charAt(pos + 1) == '*' || text.charAt(pos + 1) == '_')) {
                        body.append(text.charAt(pos + 1));
                        pos++;
                    } else {
                        body.append(text.charAt(pos));
                    }
                }

                default -> body.append(text.charAt(pos));
            }
            pos++;
        }

        while (body.length() > 0 && body.charAt(body.length() - 1) == '\n') {
            body.setLength(body.length() - 1);
        }

        out.append(body);
    }

    private StringBuilder parseTeg(String teg) {
        StringBuilder tegBody = new StringBuilder();
        StringBuilder tegReturn = new StringBuilder();

        if (pos == text.length() - 1) {
            return new StringBuilder(teg);
        }

        arrayList.add(teg);
        pos++;

        while (pos < text.length()) {
            switch (text.charAt(pos)) {
                case '*', '_', '-', '`', '~' -> {
                    String newTeg = null;

                    switch (text.charAt(pos)) {
                        case '`', '~' -> newTeg = String.valueOf(text.charAt(pos));

                        case '-' -> {
                            if (pos < text.length() - 1 && text.charAt(pos + 1) == text.charAt(pos)) {
                                pos++;
                                newTeg = String.valueOf(text.charAt(pos)) + text.charAt(pos);
                            } else {
                                tegBody.append(text.charAt(pos));
                            }
                        }

                        default -> {
                            if (pos < text.length() - 1 && text.charAt(pos + 1) == text.charAt(pos)) {
                                pos++;
                                newTeg = String.valueOf(text.charAt(pos)) + text.charAt(pos);
                            } else {
                                newTeg = String.valueOf(text.charAt(pos));
                            }
                        }
                    }

                    if (newTeg != null) {
                        if (arrayList.contains(newTeg)) { // тег закрывающий
                            if (!arrayList.get(arrayList.size() - 1).equals(newTeg)) {
                                tegReturn.append(arrayList.get(arrayList.size() - 1)).append(tegBody).append(newTeg);
                            } else {
                                switch (newTeg) {
                                    case "*", "_" -> tegReturn.append("<em>").append(tegBody).append("</em>");
                                    case "**", "__" -> tegReturn.append("<strong>").append(tegBody).append("</strong>");
                                    case "--" -> tegReturn.append("<s>").append(tegBody).append("</s>");
                                    case "`" -> tegReturn.append("<code>").append(tegBody).append("</code>");
                                    case "~" -> tegReturn.append("<mark>").append(tegBody).append("</mark>");
                                }
                            }

                            arrayList.remove(arrayList.size() - 1);

                            return tegReturn;
                        } else { // тег открывающий
                            tegBody.append(parseTeg(newTeg));
                            String lastTeg = tegBody.substring(tegBody.length() - teg.length(), tegBody.length());

                            if (arrayList.contains(lastTeg)) {
                                if (!arrayList.get(arrayList.size() - 1).equals(newTeg)) {
                                    tegReturn.append(arrayList.get(arrayList.size() - 1)).append(tegBody).append(newTeg);
                                } else {
                                    switch (newTeg) {
                                        case "*", "_" -> tegReturn.append("<em>").append(tegBody.substring(0, tegBody.length() - teg.length())).append("</em>");
                                        case "**", "__" -> tegReturn.append("<strong>").append(tegBody.substring(0, tegBody.length() - teg.length())).append("</strong>");
                                        case "--" -> tegReturn.append("<s>").append(tegBody.substring(0, tegBody.length() - teg.length())).append("</s>");
                                        case "`" -> tegReturn.append("<code>").append(tegBody.substring(0, tegBody.length() - teg.length())).append("</code>");
                                        case "~" -> tegReturn.append("<mark>").append(tegBody.substring(0, tegBody.length() - teg.length())).append("</mark>");
                                    }
                                }

                                arrayList.remove(arrayList.size() - 1);

                                return tegReturn;
                            }
                        }
                    }
                }

                case '<' -> tegBody.append("&lt;");
                case '>' -> tegBody.append("&gt;");
                case '&' -> tegBody.append("&amp;");

                case '\\' -> {
                    if (pos < text.length() - 1 && (text.charAt(pos + 1) == '*' || text.charAt(pos + 1) == '_')) {
                        tegBody.append(text.charAt(pos + 1));
                        pos++;
                    } else {
                        tegBody.append(text.charAt(pos));
                    }
                }

                default -> tegBody.append(text.charAt(pos));
            }

            pos++;
        }

        return tegReturn.append(teg).append(tegBody);
    }

}

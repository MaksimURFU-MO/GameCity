package game;

public class NextLetter {

    public char lastSymbol(String city){
        if(city == null || city.trim().isEmpty())
            throw new IllegalArgumentException("Город не может быть пустой строкой");
        city = city.trim();
        city = city.toLowerCase();
        char lastChar = city.charAt(city.length() - 1);
        boolean end = true;
        int count = 2;
        while (end){
            if (isCorrectSymbol(lastChar) && count <= city.length()) {
                lastChar = city.charAt(city.length() - count);
                count++;
            }
            else{
                end = false;
            }
        }
        if(lastChar == 'ё')
            lastChar = 'е';
        return lastChar;
    }

    public boolean isCorrectSymbol(char lastChar){
        return (lastChar == 'ъ' || lastChar == 'ь' || lastChar == 'ы' || lastChar == 'й');
    }
}

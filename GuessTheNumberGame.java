import javax.swing.*;
import java.util.Random;

public class GuessTheNumberGame {
    private int randomNumber;
    private int maxAttempts;
    private int attempts;
    private int score;
    private int round;

    public GuessTheNumberGame(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        this.attempts = 0;
        this.score = 0;
        this.round = 1;
        generateRandomNumber();
    }

    private void generateRandomNumber() {
        Random rand = new Random();
        randomNumber = rand.nextInt(100) + 1;
    }

    private void resetGame() {
        attempts = 0;
        round++;
        generateRandomNumber();
    }

    private void startGame() {
        while (true) {
            String input = JOptionPane.showInputDialog(
                null,
                "Round: " + round + "\nEnter your guess (1-100):",
                "Guess The Number",
                JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) {
                break; // User closed the dialog
            }

            try {
                int guess = Integer.parseInt(input);
                attempts++;
                if (guess == randomNumber) {
                    JOptionPane.showMessageDialog(null, "Correct! You've guessed the number in " + attempts + " attempts.");
                    score += calculatePoints();
                    int playAgain = JOptionPane.showConfirmDialog(null, "Do you want to play another round?", "Play Again?", JOptionPane.YES_NO_OPTION);
                    if (playAgain == JOptionPane.NO_OPTION) {
                        break;
                    } else {
                        resetGame();
                    }
                } else if (guess < randomNumber) {
                    JOptionPane.showMessageDialog(null, "Too low! Try again.");
                } else {
                    JOptionPane.showMessageDialog(null, "Too high! Try again.");
                }

                if (attempts >= maxAttempts) {
                    JOptionPane.showMessageDialog(null, "Out of attempts! The number was " + randomNumber + ".");
                    int playAgain = JOptionPane.showConfirmDialog(null, "Do you want to play another round?", "Play Again?", JOptionPane.YES_NO_OPTION);
                    if (playAgain == JOptionPane.NO_OPTION) {
                        break;
                    } else {
                        resetGame();
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number between 1 and 100.");
            }
        }
        JOptionPane.showMessageDialog(null, "Game Over!\nYour total score: " + score);
    }

    private int calculatePoints() {
        return (maxAttempts - attempts + 1) * 10;
    }

    public static void main(String[] args) {
        int maxAttempts = 10;
        GuessTheNumberGame game = new GuessTheNumberGame(maxAttempts);
        game.startGame();
    }
}

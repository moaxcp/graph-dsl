package graph

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

import java.awt.image.BufferedImage

class ImageMatcher extends TypeSafeMatcher<BufferedImage> {

    private BufferedImage result
    private double percentDifference
    private double expectedPercentDifference
    private int expectedWidth
    private int expectedHeight

    static Matcher<BufferedImage> contentEqualTo(BufferedImage image) {
        return new ImageMatcher(result:image, expectedPercentDifference:0)
    }

    static Matcher<BufferedImage> contentSimilarTo(BufferedImage image, double expectedPercentDifference) {
        return new ImageMatcher(result:image, expectedPercentDifference:expectedPercentDifference)
    }

    @Override
    protected boolean matchesSafely(BufferedImage expected) {
        expectedWidth = expected.width
        expectedHeight = expected.height

        if (result.width != expected.width) {
            return false
        } else if(result.height != expected.height) {
            return false
        }

        long difference = 0
        for (int y = 0; y < result.height; y++)
        {
            for (int x = 0; x < result.width; x++)
            {
                int rgbA = result.getRGB(x, y)
                int rgbB = expected.getRGB(x, y)
                int redA = (rgbA >> 16) & 0xff
                int greenA = (rgbA >> 8) & 0xff
                int blueA = (rgbA) & 0xff
                int redB = (rgbB >> 16) & 0xff
                int greenB = (rgbB >> 8) & 0xff
                int blueB = (rgbB) & 0xff
                difference += Math.abs(redA - redB)
                difference += Math.abs(greenA - greenB)
                difference += Math.abs(blueA - blueB)
            }
        }

        // Total number of red pixels = width * height
        // Total number of blue pixels = width * height
        // Total number of green pixels = width * height
        // So total number of pixels = width * height * 3
        double total_pixels = result.width * result.height * 3

        // Normalizing the value of different pixels
        // for accuracy(average pixels per color
        // component)
        double avg_different_pixels = difference / total_pixels

        // There are 255 values of pixels in total
        percentDifference = (avg_different_pixels / 255) * 100
        if(percentDifference > expectedPercentDifference) {
            return false
        }
        return true
    }

    @Override
    void describeTo(Description description) {
        description.appendText("width: $expectedWidth, height: $expectedHeight, percentage: $expectedPercentDifference")
    }

    @Override
    void describeMismatchSafely(BufferedImage result, Description description) {
        description.appendText(" was width:$result.width, height: $result.height, percentage: $percentDifference")
    }
}

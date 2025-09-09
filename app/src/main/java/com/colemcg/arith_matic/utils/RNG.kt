import kotlin.random.Random

/**
 * A utility class for generating random numbers and shuffling lists.
 * 
 * uses kotlin.random.Random because true random is not necessary for this project
 * 
 * @author Cole McGrew
 * @version 1.0
 * @date 2025-09-08
 */
object Rng {
    val default: Random = Random.Default // default random generator from kotlin.random.Random

    /**
     * Generate a random integer between min and maxInclusive
     * 
     * @param min the minimum value of the random integer
     * @param maxInclusive the maximum value of the random integer
     * @return a random integer between min and maxInclusive
     */
    fun int(min: Int, maxInclusive: Int): Int = 
        Random.nextInt(from = min, until = maxInclusive + 1)
    
    /**
     * Generate a random integer between 0 and maxInclusive
     * 
     * @param maxInclusive the maximum value of the random integer
     * @return a random integer between 0 and maxInclusive
     */
    fun int(maxInclusive: Int): Int =
        Random.nextInt(from = 0, until = maxInclusive + 1)

    /**
     * Generate a random float between 0 and 1
     * 
     * @return a random float between 0 and 1
     */
    fun unit(): Float =
        Random.nextFloat()

    /**
     * Choose a random element from a list
     * 
     * @param list the list to choose an element from
     * @return a random element from a list
     */
    fun <T> choose(list: List<T>): T =
        list[Random.nextInt(list.size)]

    /**
     * Shuffle a list
     * 
     * @param list the list to shuffle
     * @return the shuffled list, used for shuffling the question list
     */
    fun <T> shuffle(list: MutableList<T>) = 
        list.shuffle(Random)
}
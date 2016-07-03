import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GenomeRearrangement {
	
	public static final int MAXIMUM_NUMBER_OF_TIMES_TO_FLIP = 1000;
	
	private Random randomNumberGenerator;
	private int numberOfTimesToFlip;
	
	/**
	 * Constructor
	 */
	public GenomeRearrangement() {
		
		this.randomNumberGenerator = new Random();
		this.numberOfTimesToFlip = randomNumberGenerator.nextInt(MAXIMUM_NUMBER_OF_TIMES_TO_FLIP);
		
	}
	
	/**
	 * @param sequenceLength
	 * @return a Genomic Sequence of the given length formed by flipping a random section of the sequence multiple times 
	 */
	public List<Integer> getGenomicSequence(int sequenceLength) {
		
		assert sequenceLength > 0 : "Genonmic sequence length " + sequenceLength + " cannot be negative.";
		
		List<Integer> genomicSequence = getOrderedGenomicSequence(sequenceLength);
		
		int flipStart = 0, flipEnd = 0;
		for (int flipCounter = 0; flipCounter < this.numberOfTimesToFlip; ++flipCounter) {
			flipStart = this.randomNumberGenerator.nextInt(sequenceLength);
			flipEnd = this.randomNumberGenerator.nextInt(sequenceLength);
			genomicSequence = oneFlip(genomicSequence, flipStart, flipEnd);
		}	
		
		return genomicSequence;
		
	}
	
	/**
	 * @param sequenceLength
	 * @return an ordered genomic sequence starting at 1 and ending at sequenceLength
	 */
	private List<Integer> getOrderedGenomicSequence(int sequenceLength) {
		
		List<Integer> orderedGenomicSequence = new ArrayList<Integer>(sequenceLength);
		for (int counter = 0; counter < sequenceLength;) {
			orderedGenomicSequence.set(counter, ++counter);
		}
		return orderedGenomicSequence;
		
	}
	
	/**
	 * @param genomicSequence
	 * @param flipStart
	 * @param flipEnd
	 * @return Flip the part of the sequence from flipStart to flipEnd
	 */
	private List<Integer> oneFlip(List<Integer> genomicSequence, int flipStart, int flipEnd) {
		
		List<Integer> subSequenceToFlip = new ArrayList<Integer>(Math.abs(flipEnd - flipStart) + 1);
		
		
		
		
		
		
		return genomicSequence;
		
	}
	
}

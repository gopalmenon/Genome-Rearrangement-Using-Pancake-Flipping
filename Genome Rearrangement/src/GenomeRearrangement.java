import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class GenomeRearrangement {
	
	public static final int MAXIMUM_NUMBER_OF_TIMES_TO_FLIP = 1000;
	
	private Random randomNumberGenerator;
	private int numberOfTimesToFlip;
	private int sequenceLength;
	private List<Integer> genomicSequence;
	private List<Integer> identityGenomicSequence;
	
	/**
	 * Constructor
	 */
	public GenomeRearrangement(int sequenceLength) {
		
		this.randomNumberGenerator = new Random();
		this.numberOfTimesToFlip = randomNumberGenerator.nextInt(MAXIMUM_NUMBER_OF_TIMES_TO_FLIP);
		this.sequenceLength = sequenceLength;
		this.identityGenomicSequence = getOrderedGenomicSequence(this.sequenceLength);
		this.genomicSequence = generateGenomicSequence(this.sequenceLength);
	}
	
	/**
	 * @return genomic sequence that is created by flipping subsections of the identity sequence multiple times
	 */
	public List<Integer> getGenomicSequence() {
		return Collections.unmodifiableList(this.genomicSequence);
	}
	
	/**
	 * @return the number of times subsections of the identity sequence were flipped to generate the genomic sequence
	 */
	public int getNumberOfFlipsToGenerateGenomicSequence() {
		return this.numberOfTimesToFlip;
	}
	
	/**
	 * @param sequenceLength
	 * @return a Genomic Sequence of the given length formed by flipping a random section of the sequence multiple times 
	 */
	private List<Integer> generateGenomicSequence(int sequenceLength) {
		
		assert sequenceLength > 0 : "Genonmic sequence length " + sequenceLength + " cannot be negative.";
		
		List<Integer> genomicSequence = this.identityGenomicSequence;
		
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
		for (int counter = 0; counter < sequenceLength; ++counter) {
			orderedGenomicSequence.add(counter + 1);
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
		
		if (flipStart == flipEnd) {
			return genomicSequence;
		}
		
		int startIndex = flipStart < flipEnd ? flipStart : flipEnd;
		int endIndex = flipStart < flipEnd ? flipEnd : flipStart;
		
		//Get a copy of the subsequence to flip
		List<Integer> sublist = genomicSequence.subList(startIndex, endIndex + 1);
		List<Integer> subSequenceToFlip = new ArrayList<Integer>(Math.abs(flipEnd - flipStart) + 1);
		for (Integer genomeEntry : sublist) {
			subSequenceToFlip.add(genomeEntry);
		}
		
		//Go through copy of subsequence in reverse order and put in original sequence
		int subsequenceLength = subSequenceToFlip.size();
		for (int counter = subsequenceLength -1; counter >= 0; --counter) {
			genomicSequence.set(startIndex++, subSequenceToFlip.get(counter));
		}
		
		return genomicSequence;
		
	}
	
}

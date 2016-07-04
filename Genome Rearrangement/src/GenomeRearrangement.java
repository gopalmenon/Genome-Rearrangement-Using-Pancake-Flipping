import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GenomeRearrangement {
	
	public static final int MAXIMUM_NUMBER_OF_TIMES_TO_FLIP = 1000;
	public static final String GENOMIC_SEQUENCE_DIRECTIONS = "GENOMIC_SEQUENCE_DIRECTIONS";
	public static final String GENOMIC_SEQUENCE_BOUNDARIES = "GENOMIC_SEQUENCE_BOUNDARIES";
	public static final String DESCENDING_STRIP = "D";
	public static final String ASCENDING_STRIP = "A";
	public static final String STRIP_BOUNDARY = "|";
	
	private Random randomNumberGenerator;
	private int numberOfTimesToFlip;
	private int sequenceLength;
	private List<Integer> identityGenomicSequence;
	private List<Integer> genomicSequence;
	private int numberOfFlipsToRevertToIdentity;
	
	/**
	 * Constructor
	 */
	public GenomeRearrangement(int sequenceLength) {
		
		this.randomNumberGenerator = new Random();
		this.numberOfTimesToFlip = randomNumberGenerator.nextInt(MAXIMUM_NUMBER_OF_TIMES_TO_FLIP);
		this.sequenceLength = sequenceLength;
		this.identityGenomicSequence = getOrderedGenomicSequence(this.sequenceLength);
		this.genomicSequence = generateGenomicSequence(this.sequenceLength);
		revertToIdentitySequence();
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
	
	public int getnumberOfFlipsToRevertToIdentity() {
		return this.numberOfFlipsToRevertToIdentity;
	}
	
	/**
	 * @return Number of flips for the class variable genomic sequence to revert to Identity Sequence
	 */
	private void revertToIdentitySequence() {
		
		this.numberOfFlipsToRevertToIdentity = getNumberOfFlipsToRevertToIdentitySequence(this.genomicSequence);
		
	}
	
	/**
	 * @param genomicSequence
	 * @return Number of flips for the input genomic sequence to revert to Identity Sequence
	 */
	public static int getNumberOfFlipsToRevertToIdentitySequence(List<Integer> genomicSequence) {
		
		markGenomicSequenceBreakpoints(genomicSequence);
		
		
		
		return 0;
		
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
	
	/**
	 * Mark the boundaries in the genomic sequence
	 */
	private static Map<String, String[]> markGenomicSequenceBreakpoints(List<Integer> genomicSequence) {
		
		Map<String, String[]> genomicSequenceBreakpoints = new HashMap<String, String[]>();
		int genomicSequenceLength = genomicSequence.size();
		String[] genomicSequenceElementDirections = new String[genomicSequenceLength];
		String[] genomicSequenceElementBoundaries = new String[genomicSequenceLength];
		
		//Start from the beginning of the sequence and mark the breakpoints
		for (int index = 1; index < genomicSequenceLength; ++index) {
			if (genomicSequence.get(index - 1).intValue() == genomicSequence.get(index).intValue() - 1) {
				genomicSequenceElementDirections[index - 1] = ASCENDING_STRIP;
				genomicSequenceElementDirections[index] = ASCENDING_STRIP;
			} else if (genomicSequence.get(index - 1).intValue() == genomicSequence.get(index).intValue() + 1) {
				genomicSequenceElementDirections[index - 1] = DESCENDING_STRIP;
				genomicSequenceElementDirections[index] = DESCENDING_STRIP;
			} else if (index == 1) {
				genomicSequenceElementDirections[index - 1] = ASCENDING_STRIP;
				genomicSequenceElementBoundaries[index - 1] = STRIP_BOUNDARY;
			} else if (index == genomicSequenceLength - 1) {
				genomicSequenceElementDirections[index] = ASCENDING_STRIP;
				genomicSequenceElementBoundaries[index - 1] = STRIP_BOUNDARY;
			} else {
				if (genomicSequenceElementDirections[index - 1] == null) {
					genomicSequenceElementDirections[index - 1] = DESCENDING_STRIP;
				}
				genomicSequenceElementBoundaries[index - 1] = STRIP_BOUNDARY;
			}
		}
		
		genomicSequenceBreakpoints.put(GENOMIC_SEQUENCE_DIRECTIONS, genomicSequenceElementDirections);
		genomicSequenceBreakpoints.put(GENOMIC_SEQUENCE_BOUNDARIES, genomicSequenceElementBoundaries);
		return genomicSequenceBreakpoints;
		
	}
}

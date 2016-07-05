import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GenomeRearrangement {
	
	public static final int MAXIMUM_NUMBER_OF_TIMES_TO_FLIP = 10000;
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
		
		//Pad the genomic sequence by 0 on the left and one more than maximum value on the right
		genomicSequence = getPaddedGenomicSequence(genomicSequence);
		
		Map<String, String[]> genomicSequenceBreakpoints = markGenomicSequenceBreakpoints(genomicSequence);
		
		int numberOfFlips = 0;
		//Repeat while there are still breakpoints
		System.out.println(genomicSequence);
		while (hasGenomicSequenceBreakpoints(genomicSequenceBreakpoints)) {
			
			
			if (hasDecreasingStrip(genomicSequenceBreakpoints)) {
				//If there is a decreasing strip, flip the one that maximizes the reduction in breakpoints
				genomicSequenceBreakpoints = flipStripToMinimizeBreakpoints(genomicSequence, genomicSequenceBreakpoints);
			} else {
				//Since there is no decreasing strip, flip an ascending strip
				genomicSequenceBreakpoints = flipAnAscendingStrip(genomicSequence, genomicSequenceBreakpoints);
			}
			
			//Increment the flip count
			System.out.println(genomicSequence);
			++numberOfFlips;
			
		}
		
		return numberOfFlips;
		
	}
	
	/**
	 * @param genomicSequence
	 * @return padded genomic sequence by 0 on the left and one more than maximum value on the right
	 */
	private static List<Integer> getPaddedGenomicSequence(List<Integer> genomicSequence) {
		
		int originalGenomicSequenceLength = genomicSequence.size();
		List<Integer> paddedGenomicSequence = new ArrayList<Integer>(originalGenomicSequenceLength + 2);
		int maximumSequenceElement = 0;
		for (int index = 0; index < originalGenomicSequenceLength; ++index) {
			
			assert genomicSequence.get(index).intValue() != 0 : "Genomic sequence cannot contain zero.";
			
			if (genomicSequence.get(index).intValue() > maximumSequenceElement) {
				maximumSequenceElement = genomicSequence.get(index).intValue();
			}
			
		}
		
		paddedGenomicSequence.add(Integer.valueOf(0));
		paddedGenomicSequence.addAll(genomicSequence);
		paddedGenomicSequence.add(Integer.valueOf(maximumSequenceElement + 1));
		
		return paddedGenomicSequence;
		
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
			oneFlip(genomicSequence, flipStart, flipEnd, null);
		}	
		
		return genomicSequence;
		
	}
	
	/**
	 * @param sequenceLength
	 * @return an ordered genomic sequence starting at 1 and ending at sequenceLength. The sequence will be padded on both sides by 0 and 
	 * a number one larger than the sequence length.
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
	private static Map<String, String[]> oneFlip(List<Integer> genomicSequence, int flipStart, int flipEnd, Map<String, String[]> genomicSequenceBreakpoints) {
		
		if (flipStart == flipEnd) {
			return genomicSequenceBreakpoints;
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
		
		if (genomicSequenceBreakpoints != null) {
			return markGenomicSequenceBreakpoints(genomicSequence);
		} else {
			return null;
		}
		
	}
	
	/**
	 * Mark the strip directions and boundaries in the genomic sequence
	 */
	private static Map<String, String[]> markGenomicSequenceBreakpoints(List<Integer> genomicSequence) {
		
		Map<String, String[]> genomicSequenceBreakpoints = new HashMap<String, String[]>();
		int genomicSequenceLength = genomicSequence.size();
		String[] genomicSequenceElementDirections = new String[genomicSequenceLength];
		String[] genomicSequenceElementBoundaries = new String[genomicSequenceLength];
		
		//Start from the beginning of the sequence and mark the breakpoints
		for (int index = 1; index < genomicSequenceLength; ++index) {
			//If previous is one less than current, mark both as ascending
			if (genomicSequence.get(index - 1).intValue() == genomicSequence.get(index).intValue() - 1) {
				genomicSequenceElementDirections[index - 1] = ASCENDING_STRIP;
				genomicSequenceElementDirections[index] = ASCENDING_STRIP;
			//If previous is one greater than current, mark both as descending
			} else if (genomicSequence.get(index - 1).intValue() == genomicSequence.get(index).intValue() + 1) {
				genomicSequenceElementDirections[index - 1] = DESCENDING_STRIP;
				genomicSequenceElementDirections[index] = DESCENDING_STRIP;
			//There has to be a boundary here. If at second element, mark first as ascending
			} else if (index == 1) {
				genomicSequenceElementDirections[index - 1] = ASCENDING_STRIP;
				genomicSequenceElementBoundaries[index - 1] = STRIP_BOUNDARY;
			//There has to be a boundary here. If at last element, mark first as ascending
			} else if (index == genomicSequenceLength - 1) {
				genomicSequenceElementDirections[index] = ASCENDING_STRIP;
				genomicSequenceElementBoundaries[index - 1] = STRIP_BOUNDARY;
			} else {
				//There has to be a boundary here.
				genomicSequenceElementBoundaries[index - 1] = STRIP_BOUNDARY;
				//If previous element is not already marked, then it is a solitary element and needs to be marked as descending 
				if (genomicSequenceElementDirections[index - 1] == null) {
					genomicSequenceElementDirections[index - 1] = DESCENDING_STRIP;
				}
			}
		}
		
		genomicSequenceBreakpoints.put(GENOMIC_SEQUENCE_DIRECTIONS, genomicSequenceElementDirections);
		genomicSequenceBreakpoints.put(GENOMIC_SEQUENCE_BOUNDARIES, genomicSequenceElementBoundaries);
		return genomicSequenceBreakpoints;
		
	}
	
	/**
	 * @param genomicSequenceBreakpoints
	 * @return true if genomic sequence has breakpoints
	 */
	private static boolean hasGenomicSequenceBreakpoints(Map<String, String[]> genomicSequenceBreakpoints) {
		
		boolean hasGenomicSequenceBreakpoints = false;
		String[] breakpoints = genomicSequenceBreakpoints.get(GENOMIC_SEQUENCE_BOUNDARIES);
		for (String genomicElement : breakpoints) {
			if (genomicElement != null) {
				hasGenomicSequenceBreakpoints = true;
				break;
			}
		}
		return hasGenomicSequenceBreakpoints;
		
	}
	
	
	/**
	 * @param genomicSequenceBreakpoints
	 * @return true if genomic sequence has a decreasing strip
	 */
	private static boolean hasDecreasingStrip(Map<String, String[]> genomicSequenceBreakpoints) {
		
		boolean hasDecreasingStrip = false;
		String[] sequenceDirections = genomicSequenceBreakpoints.get(GENOMIC_SEQUENCE_DIRECTIONS);
		for (String genomicElement : sequenceDirections) {
			if (DESCENDING_STRIP.equals(genomicElement)) {
				hasDecreasingStrip = true;
				break;
			}
		}
		return hasDecreasingStrip;
	}
	
	private static Map<String, String[]>  flipAnAscendingStrip(List<Integer> genomicSequence, Map<String, String[]> genomicSequenceBreakpoints) {
		
		//Find start and end indexes of the first ascending strip
		String[] breakpoints = genomicSequenceBreakpoints.get(GENOMIC_SEQUENCE_BOUNDARIES);
		int numberOfGenomicElements = breakpoints.length, flipStart = 0, flipEnd = numberOfGenomicElements - 1;
		boolean flipStartFound = false, flipEndFound = false;
		for (int index = 0; index < numberOfGenomicElements; ++index) {
			if (breakpoints[index] != null) {
				if (!flipStartFound) {
					flipStart = index + 1;
					flipStartFound = true;
				} else if (!flipEndFound) {
					flipEnd = index;
					flipEndFound = true;
					break;
				}
			}
		}
		
		//Flip the ascending strip
		return oneFlip(genomicSequence, flipStart, flipEnd, genomicSequenceBreakpoints);
	}
	
	/**
	 * @param genomicSequence
	 * @param genomicSequenceBreakpoint
	 * Flip the strip that will result in reducing the number of breakpoints by the largest amount. Among all the decreasing strips, choose the one 
	 * containing the smallest element. That will be one end of the strip to flip. The other end will be the number one smaller than it.
	 * 
	 */
	private static Map<String, String[]> flipStripToMinimizeBreakpoints(List<Integer> genomicSequence, Map<String, String[]> genomicSequenceBreakpoints) {
		
		String[] sequenceDirections = genomicSequenceBreakpoints.get(GENOMIC_SEQUENCE_DIRECTIONS);
		
		//Find the descending strip with the smallest element. That element will be at the end of the strip. 
		int smallestElementIndex = 0, smallestElement = Integer.MAX_VALUE, genomicSequenceLength = genomicSequence.size();
		for (int index = 0; index < genomicSequenceLength; ++index) {
			if (DESCENDING_STRIP.equals(sequenceDirections[index])) {
				if (genomicSequence.get(index).intValue() < smallestElement) {
					smallestElement = genomicSequence.get(index).intValue();
					smallestElementIndex = index;
				}
			}
		}
		
		//Then find the element that is one less than the smallest element found above
		int nextSmallestElementIndex = 0;
		boolean nextSmallestElementIndexFound = false;
		for (int index = 0; index < genomicSequenceLength; ++index) {
			if (genomicSequence.get(index).intValue() == smallestElement - 1) {
				nextSmallestElementIndex = index;
				nextSmallestElementIndexFound = true;
				break;
			}
		}
		
		int flipStart = 0, flipEnd = 0;
		//If next smallest element was not found, flip the descending sequence ending in the smallest element
		if (nextSmallestElementIndex == 0 && !nextSmallestElementIndexFound) {
			flipStart = getStartOfDescendingStrip(smallestElementIndex, genomicSequenceBreakpoints);
			flipEnd = smallestElementIndex;
		} else if (nextSmallestElementIndex < smallestElementIndex) {
			flipStart = nextSmallestElementIndex + 1;
			flipEnd = smallestElementIndex;
		} else {
			flipStart = smallestElementIndex + 1;
			flipEnd = nextSmallestElementIndex;
		}
		
		return oneFlip(genomicSequence, flipStart, flipEnd, genomicSequenceBreakpoints);
		
	}
	
	/**
	 * @param smallestElementIndex
	 * @param genomicSequenceBreakpoints
	 * @return starting index of the descending strip
	 */
	private static int getStartOfDescendingStrip(int smallestElementIndex, Map<String, String[]> genomicSequenceBreakpoints) {
		
		String[] breakpoints = genomicSequenceBreakpoints.get(GENOMIC_SEQUENCE_BOUNDARIES);
		int startOfDescendingStrip = 0;
		for (int index = smallestElementIndex - 1; index >= 0; --index) {
			if (breakpoints[index] != null) {
				startOfDescendingStrip = index + 1;
				break;
			}
		}
		
		return startOfDescendingStrip;
		
	}
}

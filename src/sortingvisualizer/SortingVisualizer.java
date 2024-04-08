
package sortingvisualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GraphicElement {
    ImageIcon image;
    int size; // Assuming size is still relevant for the sorting logic

    public GraphicElement(ImageIcon image, int size) {
        this.image = image;
        this.size = size; // Size could represent the image's height, width, or a custom sorting criterion
    }

    // Getter for the ImageIcon
    public ImageIcon getImage() {
        return image;
    }

    // Getter for the size
    public int getSize() {
        return size;
    }
}


public class SortingVisualizer {
    private JFrame mainFrame;
    private JComboBox<String> sortSelector;
    private JButton sortButton;
    private JPanel visualizationPanel;
    private final List<GraphicElement> elements = new ArrayList<>();
    private JButton resetButton;

    public SortingVisualizer() {
        initializeGUI();
        populateElements(); 
    }

private void initializeGUI() {
    mainFrame = new JFrame("Sorting Visualizer");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setLayout(new BorderLayout());


    String[] algorithms = {"Bubble sort", "Selection sort", "Insertion sort", "Merge sort", "Quick sort", "Heap sort"};
    sortSelector = new JComboBox<>(algorithms);
    mainFrame.add(sortSelector, BorderLayout.NORTH);

    // Initialize sortButton with its ActionListener
    sortButton = new JButton("Sort");
    sortButton.addActionListener((ActionEvent e) -> {
        String selectedAlgorithm = (String) sortSelector.getSelectedItem();
        switch (selectedAlgorithm) {
            case "Bubble sort" -> new Thread(SortingVisualizer.this::bubbleSort).start();
            case "Heap sort" -> new Thread(SortingVisualizer.this::heapSort).start();
            case "Selection sort" -> new Thread(SortingVisualizer.this::selectionSort).start();
            case "Insertion sort" -> new Thread(SortingVisualizer.this::insertSelection).start();
            
            case "Merge sort" -> new Thread(() -> mergeSort(0, elements.size() - 1)).start();
            case "Quick sort" -> new Thread(() -> quickSort(0, elements.size() - 1)).start();           
            // Implement other cases for different sorting algorithms
        }
    });

    // Initialize resetButton with its ActionListener
    resetButton = new JButton("Reset");
    resetButton.addActionListener((ActionEvent e) -> resetAndShuffle());

    // Create a panel to hold both buttons, using GridLayout to split the space evenly
    JPanel buttonPanel = new JPanel(new GridLayout(1, 2)); // 1 row, 2 columns
    buttonPanel.add(sortButton);
    buttonPanel.add(resetButton);

    // Adding an additional panel to fill only 50% of the window's width at the bottom
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(buttonPanel, BorderLayout.CENTER); // Add buttons to the center of this panel
    
    // A transparent filler panel for adjusting the buttons' width to 50% of the screen
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int fillerWidth = screenSize.width / 5; // Calculate 25% width of the screen for both sides
    JPanel fillerLeft = new JPanel();
    fillerLeft.setOpaque(false);
    fillerLeft.setPreferredSize(new Dimension(fillerWidth, 0));
    JPanel fillerRight = new JPanel();
    fillerRight.setOpaque(false);
    fillerRight.setPreferredSize(new Dimension(fillerWidth, 0));
    
    bottomPanel.add(fillerLeft, BorderLayout.WEST);
    bottomPanel.add(fillerRight, BorderLayout.EAST);

    // Add bottomPanel to the SOUTH region of the main frame
    mainFrame.add(bottomPanel, BorderLayout.SOUTH);

    visualizationPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawElements(g);
        }
    };
    visualizationPanel.setPreferredSize(new Dimension(1250, 550));
    mainFrame.add(visualizationPanel, BorderLayout.CENTER);

    mainFrame.pack();
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
}

    private void resetAndShuffle() {
    Collections.shuffle(elements); // Shuffle the elements list
    repaintPanel(); // Repaint to update the visualization
    }
    private void populateElements() {
        // Example file names; replace these with the actual image files
    String[] imageFiles = {"image1.png", "image2.png", "image3.png", "image4.png","image5.png", "image6.png", "image7.png", "image8.png"};
    List<GraphicElement> tempElements = new ArrayList<>();

    for (String fileName : imageFiles) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/" + fileName));
        // Assuming size is relevant and could be determined from the image
        int size = icon.getIconHeight(); // or any other logic to determine size
        tempElements.add(new GraphicElement(icon, size));
    }

    // Shuffle the temporary list of GraphicElement objects
    Collections.shuffle(tempElements);

    // Now add the shuffled elements to the main list
    elements.addAll(tempElements);
        
        
}
   

    private void drawElements(Graphics g) {
        int panelHeight = visualizationPanel.getHeight(); // Get the panel's current height
        int startX = 10; // Starting X coordinate for drawing images

        for (GraphicElement element : elements) {
            Image image = element.getImage().getImage();
            int imageWidth = element.getImage().getIconWidth();
            int imageHeight = element.getImage().getIconHeight();

            // Calculate startY so the image's bottom edge is at the middle of the panel's height
            int startY = (panelHeight / 2) - imageHeight; // This aligns the bottom of the image to the middle

            // Draw the image
            g.drawImage(image, startX, startY, imageWidth, imageHeight, null);

            // Move startX for the next image, adjusting spacing as needed
            startX += imageWidth + 10;
        }
    }
    private void quickSort(int low, int high) {
        if (low < high) {
            // pi is partitioning index, arr[pi] is now at right place
            int pi = partition(low, high);

            // Recursively sort elements before partition and after partition
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        GraphicElement pivot = elements.get(high);
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
            if (elements.get(j).getSize() < pivot.getSize()) {
                i++;

                // swap arr[i] and arr[j]
                GraphicElement temp = elements.get(i);
                elements.set(i, elements.get(j));
                elements.set(j, temp);

                // Visualize the swap
                repaintPanel();
                sleepForAWhile(500); // Adjust the delay as needed for visualization
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        GraphicElement temp = elements.get(i + 1);
        elements.set(i + 1, elements.get(high));
        elements.set(high, temp);

        // Visualize the final swap
        repaintPanel();
        sleepForAWhile(500); // Adjust the delay as needed for visualization

        return i + 1;
    }

    private void mergeSort(int left, int right) {
        if (left < right) {
            // Find the middle point
            int middle = (left + right) / 2;

            // Sort first and second halves
            mergeSort(left, middle);
            mergeSort(middle + 1, right);

            // Merge the sorted halves
            merge(left, middle, right);
        }
    }

    private void merge(int left, int middle, int right) {
        // Find sizes of two subarrays to be merged
        int n1 = middle - left + 1;
        int n2 = right - middle;

        /* Create temp arrays */
        List<GraphicElement> L = new ArrayList<>(n1);
        List<GraphicElement> R = new ArrayList<>(n2);

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L.add(i, elements.get(left + i));
        for (int j = 0; j < n2; ++j)
            R.add(j, elements.get(middle + 1 + j));

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = left;
        while (i < n1 && j < n2) {
            if (L.get(i).getSize() <= R.get(j).getSize()) {
                elements.set(k, L.get(i));
                i++;
            } else {
                elements.set(k, R.get(j));
                j++;
            }
            k++;
            repaintPanel(); // Visualize the merge step
            sleepForAWhile(500); // Slow down so we can see the sorting
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            elements.set(k, L.get(i));
            i++;
            k++;
            repaintPanel(); // Visualize the merge step
            sleepForAWhile(500); // Slow down so we can see the sorting
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            elements.set(k, R.get(j));
            j++;
            k++;
            repaintPanel(); // Visualize the merge step
            sleepForAWhile(500); // Slow down so we can see the sorting
        }
    }
    
    private void insertSelection(){
        int n = elements.size();
        for (int i = 1; i < n; ++i) {
            GraphicElement key = elements.get(i);
            int j = i - 1;

            /* Move elements of elements[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && elements.get(j).getSize() > key.getSize()) {
                elements.set(j + 1, elements.get(j));
                j = j - 1;
                repaintPanel(); // Visualize the step
                sleepForAWhile(500); // Slow down so we can see the sorting
            }
            elements.set(j + 1, key);
        }
    }
    private void selectionSort(){
        int n = elements.size();
    
    // One by one move the boundary of the unsorted subarray
        for (int i = 0; i < n-1; i++) {
            // Find the minimum element in the unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (elements.get(j).getSize() < elements.get(min_idx).getSize())
                    min_idx = j;

            // Swap the found minimum element with the first element
            GraphicElement temp = elements.get(min_idx);
            elements.set(min_idx, elements.get(i));
            elements.set(i, temp);

            // Visualize the step and add a delay for the animation
            repaintPanel();
            sleepForAWhile(1500); // Adjust the delay as needed
        }

    }
    // Heap sort algorithm
    private void heapSort() {
        int n = elements.size();

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(elements, n, i);

        // One by one extract an element from heap
        for (int i=n-1; i>0; i--) {
            // Move current root to end
            GraphicElement temp = elements.get(0);
            elements.set(0, elements.get(i));
            elements.set(i, temp);

            repaintPanel(); // Visualize the swap
            sleepForAWhile(1500); // Slow down the animation

            // call max heapify on the reduced heap
            heapify(elements, i, 0);
        }
    }

    // To heapify a subtree rooted with node i which is an index in arr[]. n is size of heap
    void heapify(List<GraphicElement> arr, int n, int i) {
        int largest = i; // Initialize largest as root
        int l = 2*i + 1; // left = 2*i + 1
        int r = 2*i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (l < n && arr.get(l).getSize() > arr.get(largest).getSize())
            largest = l;

        // If right child is larger than largest so far
        if (r < n && arr.get(r).getSize() > arr.get(largest).getSize())
            largest = r;

        // If largest is not root
        if (largest != i) {
            GraphicElement swap = arr.get(i);
            arr.set(i, arr.get(largest));
            arr.set(largest, swap);

            repaintPanel(); // Visualize the swap
            sleepForAWhile(1500); // Slow down the animation

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
        }
    }

    private void bubbleSort() {
        for (int i = 0; i < elements.size() - 1; i++) {
            for (int j = 0; j < elements.size() - i - 1; j++) {
                if (elements.get(j).getSize() > elements.get(j + 1).getSize()) {
                    // Swap
                    GraphicElement temp = elements.get(j);
                    elements.set(j, elements.get(j + 1));
                    elements.set(j + 1, temp);
                    repaintPanel();
                    sleepForAWhile(1000); // Sleep to visualize the swap
                }
            }
        }
    }

    private void repaintPanel() {
        SwingUtilities.invokeLater(() -> visualizationPanel.repaint());
    }

    private void sleepForAWhile(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SortingVisualizer::new);
    }
}

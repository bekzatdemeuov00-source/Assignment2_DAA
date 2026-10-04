import csv
from pathlib import Path
import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt


results_folder = Path(__file__).resolve().parent
plots_folder = results_folder / "plots"
plots_folder.mkdir(exist_ok=True)

with (results_folder / "results.csv").open(encoding="utf-8", newline="") as file:
    rows = list(csv.DictReader(file))


def plot(workload, variant, metrics, title, ylabel, filename):
    plt.figure(figsize=(8, 5))
    for structure in ("DynamicArray", "MyLinkedList", "MinHeap"):
        selected = []
        for row in rows:
            if row["workload"] == workload and row["variant"] == variant:
                if row["structure"] == structure:
                    selected.append(row)
        if not selected:
            continue
        sizes = []
        for row in selected:
            sizes.append(int(row["n"]))
        for metric in metrics:
            values = []
            for row in selected:
                values.append(float(row[metric]))
            label = structure
            if len(metrics) > 1:
                label += " " + metric
            style = "-"
            if metric == "steps":
                style = "--"
            if metric == "moves":
                style = ":"
            plt.plot(sizes, values, marker="o", linestyle=style, label=label)
    plt.xscale("log")
    if metrics == ["time_ms"]:
        plt.yscale("log")
        ylabel += " (log scale)"
    plt.xticks([100, 1000, 10000, 100000], ["100", "1000", "10000", "100000"])
    plt.title(title)
    plt.xlabel("Initial number of elements (n, log scale)")
    plt.ylabel(ylabel)
    plt.legend()
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig(plots_folder / filename, dpi=150)
    plt.close()
    print("Saved", filename)


plot("W1", "-", ["time_ms"], "W1: Random access time", "Median time (ms)", "w1_time.png")
plot("W1", "-", ["steps"], "W1: Random access steps", "Steps", "w1_steps.png")
plot("W2", "-", ["time_ms"], "W2: Search time", "Median time (ms)", "w2_time.png")
plot("W2", "-", ["steps", "comparisons"], "W2: Search operations", "Operations", "w2_operations.png")
for variant in ("head", "middle"):
    plot("W3", variant, ["time_ms"], "W3: " + variant + " time", "Median time (ms)", "w3_" + variant + "_time.png")
    plot("W3", variant, ["steps", "moves"], "W3: " + variant + " operations", "Operations", "w3_" + variant + "_operations.png")
plot("W4", "-", ["time_ms"], "W4: Priority processing time", "Median time (ms)", "w4_time.png")
plot("W4", "-", ["steps", "moves", "comparisons"], "W4: Priority processing operations", "Operations", "w4_operations.png")

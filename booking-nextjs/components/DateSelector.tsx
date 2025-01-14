import { DatePicker } from "antd";
import { Dayjs } from "dayjs";

interface DateSelectorProps {
    label: string;
    value: Dayjs | null;
    onChange: (date: Dayjs | null, dateString: string | string[]) => void;
}


const DateSelector: React.FC<DateSelectorProps> = ({ label, value, onChange }) => {
    return (
        <div className="mb-5">
            <label className="block text-gray-800 dark:text-white font-medium mb-2">
                {label}
            </label>
            <DatePicker
                value={value} // Controlar el valor del DatePicker
                onChange={onChange}
                format="YYYY-MM-DD"
                className="p-2 border rounded-md"
            />
        </div>
    );
};

export default DateSelector;

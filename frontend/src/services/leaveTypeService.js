import { useEffect, useState } from "react";
import { getLeaveTypes } from "../services/leaveTypeService";

function ApplyLeave() {
    const [leaveTypes, setLeaveTypes] = useState([]);

    useEffect(() => {
        const loadLeaveTypes = async () => {
            try {
                const data = await getLeaveTypes();
                setLeaveTypes(data);
            } catch (error) {
                console.error("Failed to load leave types", error);
            }
        };

        loadLeaveTypes();
    }, []);

    return (
        <div>
            <h1>Apply Leave</h1>

            <select>
                <option value="">Select leave type</option>

                {leaveTypes.map((type) => (
                    <option key={type.id} value={type.id}>
                        {type.name}
                    </option>
                ))}
            </select>
        </div>
    );
}

export default ApplyLeave;